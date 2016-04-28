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
import core.ast.StatementSequence;
import core.ast.Term;
import core.ast.WhileStatement;
import core.ast.WriteInt;

public interface Visitor {
	public void visit(ASTNode astNode);
	public void visit(Program program);
	public void visit(StatementSequence statementSequence);
	public void visit(Declarations declarations);
	public void visit(Declaration declaration);
	public void visit(Identifier ident);
	public void visit(Assignment assignment);
	public void visit(IfStatement ifStatement);
	public void visit(WhileStatement whileStatement);
	public void visit(WriteInt writeInt);
	public void visit(ReadInt readInt);
	public void visit(ElseClause elseClause);
	public void visit(Expression expression);
	public void visit(SimpleExpression simpleExpression);
	public void visit(Term term);
	public void visit(Factor factor);
}
