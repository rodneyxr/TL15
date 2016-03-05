package core;

public class Token {

	private TokenType type;
	private String text;

	public Token(TokenType type) {
		this(type, null);
	}

	public Token(TokenType type, String text) {
		this.type = type;
		this.text = text;
	}

	public String toString() {
		if (text != null) {
			return String.format("%s(%s)", type.toString(), text);
		}
		return type.toString();
	}

}
