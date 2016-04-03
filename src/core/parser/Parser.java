package core.parser;

import core.lexer.Token;
import core.lexer.TokenStream;

public class Parser {

	public void token(Token token, TokenStream stream, TreeNode node) {
		if (stream.next() == token) {
			TreeNode tokenNode = new TreeNode(null, token);
			node.addChild(tokenNode);
		} else {
			System.err.println("PARSER ERROR");
		}
	}

	public void prog(TokenStream stream, TreeNode node) {
		// TODO: Add parsing functions as per TL 15.0 specs
	}
	
}
