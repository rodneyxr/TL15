package core.ast;

public enum DeclarationType {
	INT("int"), BOOL("bool");

	String str;

	DeclarationType(String str) {
		this.str = str;
	}
	
	@Override
	public String toString() {
		return str;
	}
}
