package core.ast;

public enum IdentifierType {
	BOOL("bool"), INT("int"), VOID("void");

	String str;

	IdentifierType(String str) {
		this.str = str;
	}

	@Override
	public String toString() {
		return str;
	}
}
