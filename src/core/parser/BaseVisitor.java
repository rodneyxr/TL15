package core.parser;

import core.ast.ASTNode;
import core.ast.Assignment;
import core.ast.Declaration;
import core.ast.Declarations;
import core.ast.ElseClause;
import core.ast.Expression;
import core.ast.Factor;
import core.ast.Identifier;
import core.ast.IfStatement;
import core.ast.Program;
import core.ast.ReadInt;
import core.ast.SimpleExpression;
import core.ast.Statement;
import core.ast.StatementSequence;
import core.ast.Term;
import core.ast.WhileStatement;
import core.ast.WriteInt;

public class BaseVisitor implements Visitor {

	@Override
	public void visit(ASTNode astNode) {
		System.out.println(astNode);
	}

	@Override
	public void visit(Program program) throws ParserException {
		program.getDeclarations().accept(this);
		program.getStatementSequence().accept(this);
	}
	
	@Override
	public void visit(StatementSequence statementSequence) throws ParserException {
		for (Statement statement : statementSequence.statements)
			statement.accept(this);
	}

	@Override
	public void visit(Declarations declarations) throws ParserException {
		for (Declaration declaration : declarations.declarations)
			declaration.accept(this);
	}

	@Override
	public void visit(Declaration declaration) throws ParserException {
		declaration.getIdent().accept(this);
	}

	@Override
	public void visit(Identifier ident) throws ParserException {
		return;
	}

	@Override
	public void visit(Assignment assignment) throws ParserException {
		assignment.getIdentifier().accept(this);
		Expression expression = assignment.getExpression();
		if (expression != null)
			expression.accept(this);
		else
			assignment.getReadInt().accept(this);
	}

	@Override
	public void visit(IfStatement ifStatement) throws ParserException {
		ifStatement.getExpression().accept(this);
		ifStatement.getStatements().accept(this);
		ElseClause elseClause = ifStatement.getElseClause();
		if (elseClause != null)
			elseClause.accept(this);
	}

	@Override
	public void visit(WhileStatement whileStatement) throws ParserException {
		whileStatement.getExpression().accept(this);
		whileStatement.getStatements().accept(this);
	}

	@Override
	public void visit(WriteInt writeInt) throws ParserException {
		writeInt.getExpression().accept(this);
	}

	@Override
	public void visit(ReadInt readInt) throws ParserException {
		return;
	}

	@Override
	public void visit(ElseClause elseClause) throws ParserException {
		elseClause.getStatements().accept(this);
	}

	@Override
	public void visit(Expression expression) throws ParserException {
		expression.getLeft().accept(this);
		Expression right = expression.getRight();
		if (right != null) {
			right.accept(this);
		}
	}

	@Override
	public void visit(SimpleExpression simpleExpression) throws ParserException {
		simpleExpression.getTerm().accept(this);
		SimpleExpression right = simpleExpression.getSimpleExpression();
		if (right != null) {
			right.accept(this);
		}
	}

	@Override
	public void visit(Term term) throws ParserException {
		term.getFactor().accept(this);
		Term right = term.getTerm();
		if (right != null) {
			right.accept(this);
		}
	}

	@Override
	public void visit(Factor factor) throws ParserException {
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
