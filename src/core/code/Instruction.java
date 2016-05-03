package core.code;

public class Instruction {

	private String op;
	private boolean isComment; // true if comment
	private boolean isLabel; // true if label
	private boolean isRaw; // true if no instructions match
	private boolean isBranch; // true if branch/jump instruction
	private boolean isBlank; // true if blank line;

	private Register r1;
	private Register r2;
	private Register r3;

	private Integer value;

	private Label label;

	public Instruction(String op) {
		if (op.matches("^\\s*#.*")) {
			isComment = true;
		} else if (op.matches("\\s*")) {
			isBlank = true;
		} else if (!op.equals("syscall")) {
			isRaw = true;
		}
		this.op = op;
	}

	public Instruction(Label label) {
		this.label = label;
		this.isLabel = true;
	}

	public Instruction(String op, Label label) {
		this.op = op;
		this.label = label;
		setBranch();
	}

	public Instruction(String op, Register r1, Label label) {
		this.op = op;
		this.r1 = r1;
		this.label = label;
		setBranch();
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

	public boolean isRaw() {
		return isRaw;
	}

	public boolean isLabel() {
		return isLabel;
	}

	public boolean isBranch() {
		return isBranch;
	}
	
	public boolean isBlank() {
		return isBlank;
	}

	public Label getLabel() {
		return label;
	}

	private void setBranch() {
		if (op.equals("beqz") || op.equals("j")) {
			isBranch = true;
		}
	}

	@Override
	public String toString() {
		if (isLabel)
			return String.format("%s:", label);

		if (op.equals("syscall") || isComment || isRaw || isBlank)
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
