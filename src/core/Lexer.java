package core;

import java.util.HashMap;
import java.util.Scanner;

public class Lexer {

	public static final HashMap<String, TokenType> SYMBOLS;

	static {
		SYMBOLS = new HashMap<String, TokenType>();
		SYMBOLS.put("(", TokenType.LP);
		SYMBOLS.put(")", TokenType.RP);
		SYMBOLS.put(":=", TokenType.ASGN);
		SYMBOLS.put(";", TokenType.SC);
		SYMBOLS.put("*", TokenType.MULTIPLICATIVE);
		SYMBOLS.put("div", TokenType.MULTIPLICATIVE);
		SYMBOLS.put("mod", TokenType.MULTIPLICATIVE);
		SYMBOLS.put("+", TokenType.ADDITIVE);
		SYMBOLS.put("-", TokenType.ADDITIVE);
		SYMBOLS.put("=", TokenType.COMPARE);
		SYMBOLS.put("!=", TokenType.COMPARE);
		SYMBOLS.put("<", TokenType.COMPARE);
		SYMBOLS.put(">", TokenType.COMPARE);
		SYMBOLS.put("<=", TokenType.COMPARE);
		SYMBOLS.put(">=", TokenType.COMPARE);
	}

	public static final HashMap<String, TokenType> KEYWORDS;

	static {
		KEYWORDS = new HashMap<String, TokenType>();
		KEYWORDS.put("if", TokenType.IF);
		KEYWORDS.put("then", TokenType.THEN);
		KEYWORDS.put("else", TokenType.ELSE);
		KEYWORDS.put("begin", TokenType.BEGIN);
		KEYWORDS.put("end", TokenType.END);
		KEYWORDS.put("while", TokenType.WHILE);
		KEYWORDS.put("do", TokenType.DO);
		KEYWORDS.put("program", TokenType.PROGRAM);
		KEYWORDS.put("var", TokenType.VAR);
		KEYWORDS.put("as", TokenType.AS);
		KEYWORDS.put("int", TokenType.INT);
		KEYWORDS.put("bool", TokenType.BOOL);
	}

	public static final HashMap<String, TokenType> BUILTINS;

	static {
		BUILTINS = new HashMap<String, TokenType>();
		BUILTINS.put("writeint", TokenType.WRITEINT);
		BUILTINS.put("readint", TokenType.READINT);
	}

	private Scanner scanner;
	private String lexeme;

	public Lexer(Scanner scanner) {
		this.scanner = scanner;
	}

	public Token getToken() {
		if (scanner.hasNext()) {
			lexeme = scanner.next();
			Token token = null;

			token = getSymbol();
			if (token != null)
				return token;

			token = getKeyword();
			if (token != null)
				return token;

			token = getBuiltin();
			if (token != null)
				return token;

			token = getNumber();
			if (token != null)
				return token;

			token = getLiteral();
			if (token != null)
				return token;

			token = getIdentifier();
			if (token != null)
				return token;

			// invalid token; lexer error
			if (token == null) {
				System.err.println("SCANNER ERROR: Invalid token -> " + lexeme);
				System.exit(1);
			}
		}
		return null;
	}

	/**
	 * 
	 * @return A symbol token if the lexeme matches.
	 */
	private Token getSymbol() {
		Token token = null;
		TokenType type = SYMBOLS.get(lexeme);
		if (type == null)
			return null;
		switch (type) {
		case MULTIPLICATIVE:
		case ADDITIVE:
		case COMPARE:
			token = new Token(type, lexeme);
			break;
		default:
			token = new Token(type);
			break;
		}
		return token;
	}

	/**
	 * 
	 * @return A keyword token if the lexeme matches.
	 */
	private Token getKeyword() {
		Token token = null;
		TokenType type = KEYWORDS.get(lexeme);
		if (type == null)
			return null;
		token = new Token(type);
		return token;
	}

	/**
	 * 
	 * @return A built-in token if the lexeme matches.
	 */
	private Token getBuiltin() {
		Token token = null;
		TokenType type = BUILTINS.get(lexeme);
		if (type == null)
			return null;
		token = new Token(type);
		return token;
	}

	/**
	 * Gets a valid number matching the following regular expression:
	 * [1-9][0-9]*|0
	 * 
	 * The regular expression allows all natural numbers, but since we are using
	 * 32-bit integers. Only 0 through 2147483647 are valid integer constants.
	 * Integer constants outside of that range should be flagged as illegal.
	 * 
	 * @return A number token if the lexeme matches the regular expression and
	 *         lies within the 32-bit integer restraints.
	 */
	private Token getNumber() {
		Token token = null;
		if (lexeme.matches("[1-9][0-9]*|0")) {
			try {
			Integer.valueOf(lexeme);
			} catch (NumberFormatException e) {
				System.err.println("SCANNER ERROR: Number too large -> " + lexeme);
				System.exit(1);
			}
			token = new Token(TokenType.num, lexeme);
		}
		return token;
	}

	/**
	 * 
	 * @return A Literal token if the lexeme matches true|false.
	 */
	private Token getLiteral() {
		Token token = null;
		if (lexeme.matches("true|false")) {
			token = new Token(TokenType.boollit, lexeme);
		}
		return token;
	}

	/**
	 * Gets a valid identifier matching the following regular expression:
	 * [A-Z][A-Z0-9]*
	 * 
	 * @return An identifier token if the lexeme matches the regular expression.
	 */
	private Token getIdentifier() {
		Token token = null;
		if (lexeme.matches("[A-Z][A-Z0-9]*")) {
			token = new Token(TokenType.ident, lexeme);
		}
		return token;
	}

}
