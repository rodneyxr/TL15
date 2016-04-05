package core.lexer;

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

	public boolean isType(TokenType type) {
		return type == this.type;
	}

	public TokenType getType() {
		return type;
	}

	public String toString() {
		if (text != null) {
			return String.format("%s(%s)", type.toString(), text);
		}
		return type.toString();
	}

}
