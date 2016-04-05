package core.parser;

import core.lexer.Token;
import core.lexer.TokenStream;
import core.lexer.TokenType;

// TODO: Add parsing functions as per TL 15.0 specs
public class Parser {

	private TokenStream stream;

	public Parser(TokenStream stream) {
		this.stream = stream;
	}

	public void token(TokenType type, TreeNode node) {
		if (stream.token().isType(type)) {
			TreeNode tokenNode = new TreeNode(null, stream.token());
			node.addChild(tokenNode);
			stream.next();
		} else {
			System.err.println("PARSER ERROR: " + stream.token() + " != " + type);
			System.exit(-1);
		}
	}

	public void program(TreeNode node) {
		Token token = stream.token();
		if (token.isType(TokenType.PROGRAM)) {
			TreeNode prog = new TreeNode("PROGRAM", token);
			node.addChild(prog);
			stream.next();

			declarations(prog);
			token(TokenType.BEGIN, prog);
			statementSequence(prog);
			token(TokenType.END, prog);
		} else {
			System.err.println("PARSER ERROR: program; " + token);
			System.exit(-1);
		}
	}

	public void declarations(TreeNode node) {
		Token token = stream.token();
		if (token.isType(TokenType.VAR)) {
			TreeNode var = new TreeNode("VAR", token);
			node.addChild(var);
			stream.next();

			token(TokenType.ident, var);
			token(TokenType.AS, var);
			type(var);
			token(TokenType.SC, var);
			declarations(var);
		} else {
			return;
		}
	}

	public void type(TreeNode node) {
		Token token = stream.token();
		if (token.isType(TokenType.INT)) {
			TreeNode intNode = new TreeNode("INT", token);
			node.addChild(intNode);
			stream.next();
		} else if (token.isType(TokenType.BOOL)) {
			TreeNode boolNode = new TreeNode("BOOL", token);
			node.addChild(boolNode);
			stream.next();
		} else {
			System.err.println("PARSER ERROR: type; " + token);
			System.exit(-1);
		}
	}

	public void statementSequence(TreeNode node) {
		Token token = stream.token();
		if (!token.isType(TokenType.END)) {
			statement(node);
			token(TokenType.SC, node);
			statementSequence(node);
		} else {
			return;
		}
	}

	public void statement(TreeNode node) {
		Token token = stream.token();
		TreeNode statementNode = new TreeNode("STATEMENT", null);
		node.addChild(statementNode);
		switch (token.getType()) {
		case ident:
			assignment(node);
			break;
		case IF:
			ifStatement(node);
			break;
		case WHILE:
			whileStatement(node);
			break;
		case WRITEINT:
			writeInt(node);
			break;
		default:
			System.err.println("PARSER ERROR: statement; " + token);
			System.exit(-1);
			break;
		}
	}

	// TODO: assignment
	public void assignment(TreeNode node) {
		Token token = stream.token();
		if (token.isType(TokenType.ident)) {
			TreeNode assignNode = new TreeNode(token.toString(), token);
			node.addChild(assignNode);
			stream.next();
			
			token(TokenType.ASGN, node);
			// FIXME: <expression> or readint
		} else {
			System.err.println("PARSER ERROR: assignment; " + token);
			System.exit(-1);
		}
	}

	// TODO: ifStatement
	public void ifStatement(TreeNode node) {

	}

	public void elseClause(TreeNode node) {

	}

	// TODO: whileStatement
	public void whileStatement(TreeNode node) {

	}

	// TODO: writeInt
	public void writeInt(TreeNode node) {

	}

	public void expression(TreeNode node) {

	}

	public void simpleExpression(TreeNode node) {

	}

	public void term(TreeNode node) {

	}

	public void factor(TreeNode node) {

	}

}
