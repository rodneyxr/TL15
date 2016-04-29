package core.parser;

import java.util.HashMap;

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

	public TypeVisitor(HashMap<String, Symbol> symbolTable) {
		this.table = symbolTable;
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
			System.exit(1);
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
			System.exit(1);
		}
	}

	@Override
	public void visit(Identifier ident) {
		Symbol symbol = table.get(ident.getVarName());
		if (symbol == null) {
			System.err.println("TYPE ERROR: undefined variable");
			System.exit(1);
		} else {
			ident.type = symbol.type;
		}
	}

	@Override
	public void visit(Assignment assignment) {
		// TODO: continue here
		assignment.getIdentifier().accept(this);
		Expression expression = assignment.getExpression();
		if (expression != null)
			expression.accept(this);
		else
			assignment.getReadInt().accept(this);
	}

	@Override
	public void visit(IfStatement ifStatement) {
		ifStatement.getExpression().accept(this);
		ifStatement.getStatements().accept(this);
		ElseClause elseClause = ifStatement.getElseClause();
		if (elseClause != null)
			elseClause.accept(this);
	}

	@Override
	public void visit(WhileStatement whileStatement) {
		whileStatement.getExpression().accept(this);
		whileStatement.getStatements().accept(this);
	}

	@Override
	public void visit(WriteInt writeInt) {
		writeInt.getExpression().accept(this);
	}

	@Override
	public void visit(ReadInt readInt) {
		return;
	}

	@Override
	public void visit(ElseClause elseClause) {
		elseClause.getStatements().accept(this);
	}

	@Override
	public void visit(Expression expression) {
		expression.getLeft().accept(this);
		Expression right = expression.getRight();
		if (right != null) {
			right.accept(this);
		}
	}

	@Override
	public void visit(SimpleExpression simpleExpression) {
		simpleExpression.getTerm().accept(this);
		SimpleExpression right = simpleExpression.getSimpleExpression();
		if (right != null) {
			right.accept(this);
		}
	}

	@Override
	public void visit(Term term) {
		term.getFactor().accept(this);
		Term right = term.getTerm();
		if (right != null) {
			right.accept(this);
		}
	}

	@Override
	public void visit(Factor factor) {
		Identifier identifier = factor.getIdent();
		if (identifier != null) {
			identifier.accept(this);
			return;
		}
		Expression expression = factor.getExpression();
		if (expression != null) {
			expression.accept(this);
		}
	}

}
