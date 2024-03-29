package core.code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import core.cfg.FlowPoint;
import core.lexer.Lexer;
import core.lexer.Token;
import core.parser.BaseVisitor;
import core.parser.ParserException;
import core.parser.Symbol;

public class CodeVisitor extends BaseVisitor {

	HashMap<String, Symbol> table;
	List<Instruction> instructions;

	private int tempReg = 0;

	private static final Register $a0 = new Register("$a0");
	private static final Register $t0 = new Register("$t0");
	private static final Register $t1 = new Register("$t1");
	private static final Register $t2 = new Register("$t2");
	private static final Register $v0 = new Register("$v0");

	public CodeVisitor(HashMap<String, Symbol> symbolTable) {
		this.table = symbolTable;
		this.instructions = new ArrayList<>();
	}

	public FlowPoint generateCFG() {
		HashMap<Label, FlowPoint> map = new HashMap<>();
		List<FlowPoint> branches = new ArrayList<>();
		FlowPoint cfg = null;
		FlowPoint node = null;

		boolean startBlock = false; // skip the header
		for (Instruction i : instructions) {
			if (i.isBlank())
				continue;

			// ignore the header
			if (!startBlock) {
				if (i.isLabel() && i.getLabel().toString().equals("StartBlock")) {
					startBlock = true;
					cfg = new FlowPoint();
					node = cfg;
					node.addInstruction(i);
					map.put(i.getLabel(), node);
				}
				continue;
			}

			if (i.isLabel()) {
				FlowPoint last = node;
				if (!node.isEmpty()) {
					node = new FlowPoint();
					last.addFlowPoint(node);
				}
				node.addInstruction(i);
				map.put(i.getLabel(), node);
			} else if (i.isBranch()) {
				node.addInstruction(i);
				node.setBranchLabel(i.getLabel());
				branches.add(node);
				FlowPoint last = node;
				node = new FlowPoint();
				last.addFlowPoint(node);
			} else {
				node.addInstruction(i);
			}
		}

		// link branches to label flow points
		for (FlowPoint fp : branches) {
			fp.addFlowPoint(map.get(fp.getBranchLabel()));
		}
		return cfg;
	}

	public String getCode() {
		StringBuilder sb = new StringBuilder();
		for (Instruction i : instructions) {
			if (!i.isRaw() && !i.isLabel())
				sb.append("    ");
			sb.append(i);
			sb.append('\n');
		}
		return sb.toString();
	}

	private void emit(String s) {
		instructions.add(new Instruction(s));
	}

	private void emit(Instruction instruction) {
		instructions.add(instruction);
	}

	private void emit(Label label) {
		instructions.add(new Instruction(label));
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
		emit(new Label("StartBlock"));
		emit("");
		program.getDeclarations().accept(this);
		program.getStatementSequence().accept(this);
		emit("# exit");
		emit(new Instruction("li", $v0, 10));
		emit(new Instruction("syscall"));
	}

	@Override
	public void visit(Declaration declaration) throws ParserException {
		declaration.getIdent().accept(this);
		Register r1 = declaration.getIdent().reg;
		emit("# loadI 0 => r_" + declaration.getIdent().getVarName());
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
			emit("# assignment => r_" + assignment.getIdentifier().getVarName());
			emit(new Instruction("lw", $t0, res));
		} else {
			emit("# readInt => r_" + assignment.getIdentifier().getVarName());
			assignment.getReadInt().accept(this);
			// readInt will store input into $v0
			emit(new Instruction("move", $t0, $v0));
		}
		emit(new Instruction("sw", $t0, var));
		emit("");
	}

	@Override
	public void visit(IfStatement ifStatement) throws ParserException {
		ElseClause elseClause = ifStatement.getElseClause();
		ifStatement.getExpression().accept(this);
		Label[] ifLabels = Label.nextIfLabels();
		Label elseLabel = ifLabels[0];
		Label endIfLabel = ifLabels[1];
		Register res = ifStatement.getExpression().reg;
		emit("# IfStatement");
		emit(new Instruction("lw", $t0, res));
		if (elseClause != null) {
			emit(new Instruction("beqz", $t0, elseLabel));
		} else {
			emit(new Instruction("beqz", $t0, endIfLabel));
		}
		emit("");

		ifStatement.getStatements().accept(this);

		if (elseClause != null) {
			emit(new Instruction("j", endIfLabel));
			emit("");
			emit(elseLabel);
			emit("");
			elseClause.accept(this);
			emit(endIfLabel);
		} else {
			emit(endIfLabel);
		}
		emit("");
	}

	@Override
	public void visit(WhileStatement whileStatement) throws ParserException {
		Label[] labels = Label.nextWhileLabels();
		Label whileStart = labels[0];
		Label whileEnd = labels[1];
		emit(whileStart);
		emit("");

		whileStatement.getExpression().accept(this);
		Register res = whileStatement.getExpression().reg;
		emit("# WhileStatement");
		emit(new Instruction("lw", $t0, res));
		emit(new Instruction("beqz", $t0, whileEnd));
		emit("");

		whileStatement.getStatements().accept(this);
		emit(new Instruction("j", whileStart));
		emit("");
		emit(whileEnd);
		emit("");
	}

	@Override
	public void visit(WriteInt writeInt) throws ParserException {
		writeInt.getExpression().accept(this);
		Register r1 = writeInt.getExpression().reg;
		emit("# writeInt");
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
	}

	@Override
	public void visit(Expression expression) throws ParserException {
		expression.getLeft().accept(this);
		Expression right = expression.getRight();

		if (right != null) {
			right.accept(this);
			Register res = Register.next();
			expression.reg = res;
			Register r1 = expression.getLeft().reg;
			Register r2 = right.reg;
			switch (expression.getCompare().getText()) {
			case Lexer.EQ:
				emit("# EQ");
				emit(new Instruction("lw", $t1, r1));
				emit(new Instruction("lw", $t2, r2));
				emit(new Instruction("seq", $t0, $t1, $t2));
				emit(new Instruction("sw", $t0, res));
				break;
			case Lexer.NE:
				emit("# NE");
				emit(new Instruction("lw", $t1, r1));
				emit(new Instruction("lw", $t2, r2));
				emit(new Instruction("sne", $t0, $t1, $t2));
				emit(new Instruction("sw", $t0, res));
				break;
			case Lexer.LT:
				emit("# LT");
				emit(new Instruction("lw", $t1, r1));
				emit(new Instruction("lw", $t2, r2));
				emit(new Instruction("slt", $t0, $t1, $t2));
				emit(new Instruction("sw", $t0, res));
				break;
			case Lexer.GT:
				emit("# GT");
				emit(new Instruction("lw", $t1, r1));
				emit(new Instruction("lw", $t2, r2));
				emit(new Instruction("sgt", $t0, $t1, $t2));
				emit(new Instruction("sw", $t0, res));
				break;
			case Lexer.LE:
				emit("# LE");
				emit(new Instruction("lw", $t1, r1));
				emit(new Instruction("lw", $t2, r2));
				emit(new Instruction("sle", $t0, $t1, $t2));
				emit(new Instruction("sw", $t0, res));
				break;
			case Lexer.GE:
				emit("# GE");
				emit(new Instruction("lw", $t1, r1));
				emit(new Instruction("lw", $t2, r2));
				emit(new Instruction("sge", $t0, $t1, $t2));
				emit(new Instruction("sw", $t0, res));
				break;
			}
			emit("");
		} else {
			expression.reg = expression.getLeft().reg;
		}
	}

	@Override
	public void visit(SimpleExpression simpleExpression) throws ParserException {
		simpleExpression.getTerm().accept(this);
		SimpleExpression right = simpleExpression.getSimpleExpression();

		if (right != null) {
			right.accept(this);
			Register res = Register.next();
			simpleExpression.reg = res;
			Register r1 = simpleExpression.getTerm().reg;
			Register r2 = right.reg;
			switch (simpleExpression.getAdditive().getText()) {
			case Lexer.ADD:
				emit("# add");
				emit(new Instruction("lw", $t1, r1));
				emit(new Instruction("lw", $t2, r2));
				emit(new Instruction("add", $t0, $t1, $t2));
				emit(new Instruction("sw", $t0, res));
				break;
			case Lexer.SUB:
				emit("# subtract");
				emit(new Instruction("lw", $t1, r1));
				emit(new Instruction("lw", $t2, r2));
				emit(new Instruction("sub", $t0, $t1, $t2));
				emit(new Instruction("sw", $t0, res));
				break;
			}
			emit("");
		} else {
			simpleExpression.reg = simpleExpression.getTerm().reg;
		}
	}

	@Override
	public void visit(Term term) throws ParserException {
		term.getFactor().accept(this);
		Term right = term.getTerm();

		if (right != null) {
			right.accept(this);
			Register res = Register.next();
			term.reg = res;
			Register r1 = term.getFactor().reg;
			Register r2 = right.reg;
			switch (term.getMultiplicative()) {
			case Lexer.MULT:
				emit("# multiply");
				emit(new Instruction("lw", $t1, r1));
				emit(new Instruction("lw", $t2, r2));
				emit(new Instruction("mul", $t0, $t1, $t2));
				emit(new Instruction("sw", $t0, res));
				break;
			case Lexer.DIV:
				emit("# divide");
				emit(new Instruction("lw", $t1, r1));
				emit(new Instruction("lw", $t2, r2));
				emit(new Instruction("div", $t0, $t1, $t2));
				emit(new Instruction("sw", $t0, res));
				break;
			case Lexer.MOD:
				emit("# modulus");
				emit(new Instruction("lw", $t1, r1));
				emit(new Instruction("lw", $t2, r2));
				emit(new Instruction("div", $t0, $t1, $t2));
				emit(new Instruction("mul", $t0, $t0, $t2));
				emit(new Instruction("sub", $t0, $t1, $t0));
				emit(new Instruction("sw", $t0, res));
				break;
			}
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
			emit(String.format("# loadI %d => r%d", val, tempReg++));
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
			emit(String.format("# loadI %d => r%d", val, tempReg++));
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
