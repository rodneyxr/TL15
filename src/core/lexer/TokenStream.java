package core.lexer;

import java.util.ArrayList;
import java.util.List;

public class TokenStream {

	private List<Token> tokens = new ArrayList<>();
	private int position = 0;

	/**
	 * Takes a Lexer and adds the tokens to this stream.
	 * 
	 * @param lexer
	 *            The lexer to tokenize source and fill the stream.
	 */
	public TokenStream(Lexer lexer) {
		Token token = lexer.getToken();
		while (token != null) {
			tokens.add(token);
			token = lexer.getToken();
		}
	}

	/**
	 * Get the token at the specified position in the stream.
	 * 
	 * @param position
	 *            the nth token in the stream.
	 * @return The token at the specified position in the stream.
	 */
	public Token get(int position) {
		if (position < 0 || position > tokens.size() - 1)
			return null;
		return tokens.get(position);
	}

	/**
	 * Gets the next token in the stream.
	 * 
	 * @return The next token in the stream. Null if end of stream.
	 */
	public Token next() {
		return get(position + 1);
	}

	/**
	 * Gets the previous token in the stream.
	 * 
	 * @return The previous token in the stream. Null if at beginning of stream.
	 */
	public Token previous() {
		return get(position - 1);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Token token : tokens)
			sb.append(token).append('\n');
		return sb.toString();
	}

}
