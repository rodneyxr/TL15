package core.parser;

import java.util.HashMap;

import core.ast.ASTNode;
import core.ast.Assignment;
import core.ast.Declaration;
import core.ast.Declarations;
import core.ast.ElseClause;
import core.ast.Expression;
import core.ast.Factor;
import core.ast.Identifier;
import core.ast.IdentifierType;
import core.ast.IfStatement;
import core.ast.Program;
import core.ast.ReadInt;
import core.ast.SimpleExpression;
import core.ast.Statement;
import core.ast.StatementSequence;
import core.ast.Term;
import core.ast.WhileStatement;
import core.ast.WriteInt;

public class TypeVisitor extends BaseVisitor {

	HashMap<String, Symbol> table;
	boolean typeError;

	public TypeVisitor(HashMap<String, Symbol> symbolTable) {
		this.table = symbolTable;
	}

	private void typeError(ASTNode node) {
		node.typeError = true;
		typeError = true;
	}

	public boolean hasTypeError() {
		return typeError;
	}

	@Override
	public void visit(Program program) {
		program.getDeclarations().accept(this);
		program.getStatementSequence().accept(this);
		if (program.getDeclarations().type.equals(IdentifierType.VOID)
				&& program.getStatementSequence().type.equals(IdentifierType.VOID)) {
			program.type = IdentifierType.VOID;
		} else {
			System.err.println("TYPE ERROR: program");
			typeError(program);
			// System.exit(1);
		}
	}

	@Override
	public void visit(StatementSequence statementSequence) {
		for (Statement statement : statementSequence.statements)
			statement.accept(this);
		statementSequence.type = IdentifierType.VOID;
	}

	@Override
	public void visit(Declarations declarations) {
		for (Declaration declaration : declarations.declarations)
			declaration.accept(this);
		declarations.type = IdentifierType.VOID;
	}

	@Override
	public void visit(Declaration declaration) {
		declaration.getIdent().accept(this);
		IdentifierType type = declaration.getIdent().type;
		if (type.equals(IdentifierType.INT) || type.equals(IdentifierType.BOOL)) {
			declaration.type = type;
		} else {
			System.err.println("TYPE ERROR: declaration");
			typeError(declaration);
			// System.exit(1);
		}
	}

	@Override
	public void visit(Identifier ident) {
		Symbol symbol = table.get(ident.getVarName());
		if (symbol == null) {
			System.err.println("TYPE ERROR: undefined variable");
			typeError(ident);
			// System.exit(1);
		} else {
			ident.type = symbol.type;
		}
	}

	@Override
	public void visit(Assignment assignment) {
		IdentifierType leftType;
		IdentifierType rightType;

		assignment.getIdentifier().accept(this);
		leftType = assignment.getIdentifier().type;
		Expression expression = assignment.getExpression();
		if (expression != null) {
			expression.accept(this);
			rightType = expression.type;
		} else {
			assignment.getReadInt().accept(this);
			rightType = assignment.getReadInt().type;
		}

		if (leftType != rightType) {
			StringBuilder errMsg = new StringBuilder("TYPE ERROR: assignment -> ");
			errMsg.append(assignment.getIdentifier());
			errMsg.append(" " + assignment + " ");
			if (expression != null)
				errMsg.append(expression);
			else
				errMsg.append(assignment.getReadInt());
			System.err.println(errMsg);
			typeError(assignment);
			// System.exit(1);
		} else {
			assignment.type = IdentifierType.VOID;
		}
	}

	@Override
	public void visit(IfStatement ifStatement) {
		ifStatement.getExpression().accept(this);
		ifStatement.getStatements().accept(this);

		IdentifierType exprType = ifStatement.getExpression().type;
		if (!(exprType.equals(IdentifierType.INT) || exprType.equals(IdentifierType.BOOL))) {
			System.err.println("TYPE ERROR: ifStatement(0)");
			typeError(ifStatement);
			// System.exit(1);
		}
		if (!ifStatement.getStatements().type.equals(IdentifierType.VOID)) {
			System.err.println("TYPE ERROR: ifStatement(1) ");
			typeError(ifStatement);
			// System.exit(1);
		}

		ElseClause elseClause = ifStatement.getElseClause();
		if (elseClause != null) {
			elseClause.accept(this);
			if (elseClause.type.equals(IdentifierType.VOID)) {
				ifStatement.type = IdentifierType.VOID;
			} else {
				System.err.println("TYPE ERROR: ifStatement(2)");
				typeError(ifStatement);
				// System.exit(1);
			}
		} else {
			ifStatement.type = IdentifierType.VOID;
		}
	}

	@Override
	public void visit(WhileStatement whileStatement) {
		whileStatement.getExpression().accept(this);
		whileStatement.getStatements().accept(this);

		IdentifierType exprType = whileStatement.getExpression().type;
		if (!(exprType.equals(IdentifierType.INT) || exprType.equals(IdentifierType.BOOL))) {
			System.err.println("TYPE ERROR: whileStatement(0)");
			typeError(whileStatement);
			// System.exit(1);
		}
		if (whileStatement.getStatements().type.equals(IdentifierType.VOID)) {
			System.err.println("TYPE ERROR: whileStatement(1)");
			typeError(whileStatement);
			// System.exit(1);
		}
	}

	@Override
	public void visit(WriteInt writeInt) {
		writeInt.getExpression().accept(this);
		IdentifierType exprType = writeInt.getExpression().type;
		if (exprType.equals(IdentifierType.INT)) {
			writeInt.type = IdentifierType.VOID;
		} else {
			System.err.println("TYPE ERROR: writeInt");
			typeError(writeInt);
			// System.exit(1);
		}
	}

	@Override
	public void visit(ReadInt readInt) {
		readInt.type = IdentifierType.INT;
	}

	@Override
	public void visit(ElseClause elseClause) {
		elseClause.getStatements().accept(this);
		if (elseClause.getStatements().type.equals(IdentifierType.VOID)) {
			elseClause.type = IdentifierType.VOID;
		} else {
			System.err.println("TYPE ERROR: elseClause");
			typeError(elseClause);
			// System.exit(1);
		}
	}

	@Override
	public void visit(Expression expression) {
		IdentifierType leftType;
		IdentifierType rightType;

		expression.getLeft().accept(this);
		leftType = expression.getLeft().type;

		Expression right = expression.getRight();
		if (right != null) {
			right.accept(this);
			rightType = right.type;
			if (leftType.equals(IdentifierType.INT) && rightType.equals(IdentifierType.INT)) {
				expression.type = IdentifierType.BOOL;
			} else {
				StringBuilder errMsg = new StringBuilder("TYPE ERROR: expression -> ");
				errMsg.append(expression.getLeft());
				if (right != null) {
					errMsg.append(" " + expression + " ");
					errMsg.append(expression.getRight());
				}
				System.err.println(errMsg);
				typeError(expression);
				// System.exit(1);
			}
		} else {
			// TODO: implement this
		}
	}

	@Override
	public void visit(SimpleExpression simpleExpression) {
		IdentifierType leftType;
		IdentifierType rightType;

		simpleExpression.getTerm().accept(this);
		leftType = simpleExpression.getTerm().type;
		SimpleExpression right = simpleExpression.getSimpleExpression();
		if (right != null) {
			right.accept(this);
			rightType = right.type;
			if (leftType.equals(IdentifierType.INT) && rightType.equals(IdentifierType.INT)) {
				simpleExpression.type = IdentifierType.INT;
			} else {
				System.err.println("TYPE ERROR: simpleExpression");
				typeError(simpleExpression);
				// System.exit(1);
			}
		} else {
			// TODO: implement this
		}
	}

	@Override
	public void visit(Term term) {
		IdentifierType leftType;
		IdentifierType rightType;

		term.getFactor().accept(this);
		leftType = term.getFactor().type;

		Term right = term.getTerm();
		if (right != null) {
			right.accept(this);
			rightType = right.type;
			if (leftType.equals(IdentifierType.INT) && rightType.equals(IdentifierType.INT)) {
				term.type = IdentifierType.INT;
			} else {
				System.err.println("TYPE ERROR: term");
				typeError(term);
				// System.exit(1);
			}
		} else {
			// TODO: implement this
		}
	}

	@Override
	public void visit(Factor factor) {
		// ident
		Identifier identifier = factor.getIdent();
		if (identifier != null) {
			identifier.accept(this);
			if (!identifier.type.equals(IdentifierType.VOID)) {
				factor.type = identifier.type;
			} else {
				System.err.println("TYPE ERROR: factor(0)");
				typeError(factor);
				// System.exit(1);
			}
			return;
		}

		// num
		if (factor.getNum() != null) {
			factor.type = IdentifierType.INT;
			return;
		}

		// bool
		if (factor.getBoollit() != null) {
			factor.type = IdentifierType.BOOL;
			return;
		}

		// LP <expression> RP
		Expression expression = factor.getExpression();
		if (expression != null) {
			expression.accept(this);
			if (!expression.type.equals(IdentifierType.VOID)) {
				factor.type = expression.type;
			} else {
				System.err.println("TYPE ERROR: factor(1)");
				typeError(factor);
				// System.exit(1);
			}
		}
	}

}
