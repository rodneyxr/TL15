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
	public void visit(ASTNode astNode) throws ParserException;
	public void visit(Program program) throws ParserException;
	public void visit(StatementSequence statementSequence) throws ParserException;
	public void visit(Declarations declarations) throws ParserException;
	public void visit(Declaration declaration) throws ParserException;
	public void visit(Identifier ident) throws ParserException;
	public void visit(Assignment assignment) throws ParserException;
	public void visit(IfStatement ifStatement) throws ParserException;
	public void visit(WhileStatement whileStatement) throws ParserException;
	public void visit(WriteInt writeInt) throws ParserException;
	public void visit(ReadInt readInt) throws ParserException;
	public void visit(ElseClause elseClause) throws ParserException;
	public void visit(Expression expression) throws ParserException;
	public void visit(SimpleExpression simpleExpression) throws ParserException;
	public void visit(Term term) throws ParserException;
	public void visit(Factor factor) throws ParserException;
}
