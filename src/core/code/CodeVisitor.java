package core.code;

import java.util.HashMap;

import core.ast.Assignment;
import core.ast.Declaration;
import core.ast.ElseClause;
import core.ast.Expression;
import core.ast.Factor;
import core.ast.Identifier;
import core.ast.IfStatement;
import core.ast.Program;
import core.ast.ReadInt;
import core.ast.SimpleExpression;
import core.ast.Term;
import core.ast.WhileStatement;
import core.ast.WriteInt;
import core.parser.BaseVisitor;
import core.parser.ParserException;
import core.parser.Symbol;

public class CodeVisitor extends BaseVisitor {

	HashMap<String, Symbol> table;
	String code = "";

	public CodeVisitor(HashMap<String, Symbol> symbolTable) {
		this.table = symbolTable;
	}

	public String getCode() {
		return code;
	}

	private void emit(String s) {
		code += s + "\n";
	}

	private void emit(Instruction instruction) {
		emit("    " + instruction);
	}

	@Override
	public void visit(Program program) throws ParserException {
		emit("    .data");
		emit("newline:	.asciiz \"\\n\"");
		emit("    .text");
		emit("    .globl main");
		emit("main:");
		emit("    li $fp, 0x7ffffffc");
		emit("");
		program.getDeclarations().accept(this);
		program.getStatementSequence().accept(this);
		emit("    # exit");
		emit("    li $v0, 10");
		emit("    syscall");
		emit("");
	}

	@Override
	public void visit(Declaration declaration) throws ParserException {
		declaration.getIdent().accept(this);
		Register r1 = new Register("$t0");
		Register r2 = declaration.getIdent().reg;
		emit("    # loadI 0 => r_" + declaration.getIdent().getVarName());
		emit(new Instruction("li", r1, 0));
		emit(new Instruction("sw", r1, r2));
		emit("");
	}

	@Override
	public void visit(Identifier ident) throws ParserException {
		Register r1 = table.get(ident.getVarName()).reg;
		if (r1 != null) {
			ident.reg = r1;
		} else {
			throw new ParserException("Identifier: " + ident.getVarName() + " not initialized");
		}
	}

	@Override
	public void visit(Assignment assignment) throws ParserException {
		// TODO: start here
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
