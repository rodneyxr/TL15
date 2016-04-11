package core.ast;

public enum StatementType {
	ASSIGNMENT(":="), IFSTATMENT("if"), WHILESTATMENT("while"), WRITEINT("writeint");

	String str;

	StatementType(String str) {
		this.str = str;
	}
	
	@Override
	public String toString() {
		return str;
	}
}
