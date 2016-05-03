package core.code;

public class Instruction {

	private String op;

	private Register r1;
	private Register r2;
	private Register r3;

	private Integer value;

	private Label label;

	public Instruction(String op) {
		if (!op.equals("syscall")) {
			System.err.println("Instruction.java: Invalid MIPS instruction");
			System.exit(1);
		}
		this.op = op;
	}

	public Instruction(String op, Label label) {
		this.op = op;
		this.label = label;
	}

	public Instruction(String op, Register r1, Label label) {
		this.op = op;
		this.r1 = r1;
		this.label = label;
	}

	public Instruction(String op, Register r1, int value) {
		this.op = op;
		this.r1 = r1;
		this.value = value;
	}

	public Instruction(String op, Register r1, Register r2) {
		this.op = op;
		this.r1 = r1;
		this.r2 = r2;
	}

	public Instruction(String op, Register r1, Register r2, Register r3) {
		this.op = op;
		this.r1 = r1;
		this.r2 = r2;
		this.r3 = r3;
	}

	@Override
	public String toString() {
		if (op.equals("syscall"))
			return op;
		if (value != null)
			return String.format("%s %s, %d", op, r1, value);
		if (label != null) {
			if (r1 != null)
				return String.format("%s %s, %s", op, r1, label);
			else
				return String.format("%s %s", op, label);
		}
		if (r3 == null)
			return String.format("%s %s, %s", op, r1, r2);
		return String.format("%s %s, %s, %s", op, r1, r2, r3);
	}

}
