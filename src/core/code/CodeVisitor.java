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
import core.lexer.Token;
import core.parser.BaseVisitor;
import core.parser.ParserException;
import core.parser.Symbol;

public class CodeVisitor extends BaseVisitor {

	HashMap<String, Symbol> table;
	String code = "";
	int tempReg = 0;

	private static final Register $a0 = new Register("$a0");
	private static final Register $t0 = new Register("$t0");
	private static final Register $t1 = new Register("$t1");
	private static final Register $t2 = new Register("$t2");
	private static final Register $v0 = new Register("$v0");

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
	}

	@Override
	public void visit(Declaration declaration) throws ParserException {
		declaration.getIdent().accept(this);
		Register r1 = declaration.getIdent().reg;
		emit("    # loadI 0 => r_" + declaration.getIdent().getVarName());
		emit(new Instruction("li", $t0, 0));
		emit(new Instruction("sw", $t0, r1));
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
		assignment.getIdentifier().accept(this);
		Expression expression = assignment.getExpression();
		Register var = assignment.getIdentifier().reg;
		Register res;
		if (expression != null) {
			expression.accept(this);
			res = expression.reg;
			emit(new Instruction("li", $t0, res));
		} else {
			emit("    # readInt => r_" + assignment.getIdentifier().getVarName());
			assignment.getReadInt().accept(this);
			// readInt will store input into $v0
			emit(new Instruction("move", $t0, $v0));
		}
		emit(new Instruction("sw", $t0, var));
		emit("");
	}

	@Override
	public void visit(IfStatement ifStatement) throws ParserException {
		ifStatement.getExpression().accept(this);
		ifStatement.getStatements().accept(this);
		ElseClause elseClause = ifStatement.getElseClause();
		if (elseClause != null)
			elseClause.accept(this);
		throw new ParserException("Not Implemented");
	}

	@Override
	public void visit(WhileStatement whileStatement) throws ParserException {
		whileStatement.getExpression().accept(this);
		whileStatement.getStatements().accept(this);
		throw new ParserException("Not Implemented");
	}

	@Override
	public void visit(WriteInt writeInt) throws ParserException {
		writeInt.getExpression().accept(this);
		Register r1 = writeInt.getExpression().reg;
		emit("    # writeInt");
		emit(new Instruction("li", $v0, 1));
		emit(new Instruction("lw", $t1, r1));
		emit(new Instruction("move", $a0, $t1));
		emit(new Instruction("syscall"));
		emit(new Instruction("li", $v0, 4));
		emit(new Instruction("la", $a0, new Register("newline")));
		emit(new Instruction("syscall"));
		emit("");
	}

	@Override
	public void visit(ReadInt readInt) throws ParserException {
		emit(new Instruction("li", $v0, 5));
		emit(new Instruction("syscall"));
	}

	@Override
	public void visit(ElseClause elseClause) throws ParserException {
		elseClause.getStatements().accept(this);
		throw new ParserException("Not Implemented");
	}

	@Override
	public void visit(Expression expression) throws ParserException {
		expression.getLeft().accept(this);
		Expression right = expression.getRight();
		if (right != null) {
			right.accept(this);
		}
		throw new ParserException("Not Implemented");
	}

	@Override
	public void visit(SimpleExpression simpleExpression) throws ParserException {
		simpleExpression.getTerm().accept(this);
		SimpleExpression right = simpleExpression.getSimpleExpression();
		if (right != null) {
			right.accept(this);
		}
		throw new ParserException("Not Implemented");
	}

	@Override
	public void visit(Term term) throws ParserException {
		term.getFactor().accept(this);
		Term right = term.getTerm();

		if (right != null) {
			// multiply
			right.accept(this);
			Register res = Register.next();
			term.reg = res;
			Register r1 = term.getFactor().reg;
			Register r2 = right.reg;
			emit("    # multiply");
			emit(new Instruction("lw", $t1, r1));
			emit(new Instruction("lw", $t2, r2));
			emit(new Instruction("mul", $t0, $t1, $t2));
			emit(new Instruction("sw", $t0, res));
			emit("");
		} else {
			term.reg = term.getFactor().reg;
		}
	}

	@Override
	public void visit(Factor factor) throws ParserException {
		// ident
		Identifier identifier = factor.getIdent();
		if (identifier != null) {
			identifier.accept(this);
			factor.reg = identifier.reg;
			return;
		}

		// num
		Token num = factor.getNum();
		if (num != null) {
			Register r1 = Register.next();
			factor.reg = r1;
			int val = Integer.valueOf(num.getText());
			emit(String.format("    # loadI %d => r%d", val, tempReg++));
			emit(new Instruction("li", $t0, val));
			emit(new Instruction("sw", $t0, r1));
			emit("");
			return;
		}

		// bool
		Token bool = factor.getBoollit();
		if (bool != null) {
			Register r1 = Register.next();
			factor.reg = r1;
			int val = 0;
			if (Boolean.valueOf(bool.getText()))
				val = 1;
			emit(String.format("    # loadI %d => r%d", val, tempReg++));
			emit(new Instruction("li", $t0, val));
			emit(new Instruction("sw", $t0, r1));
			emit("");
			return;
		}

		// LP <expression> RP
		Expression expression = factor.getExpression();
		if (expression != null) {
			expression.accept(this);
			factor.reg = expression.reg;
		}
	}

}
