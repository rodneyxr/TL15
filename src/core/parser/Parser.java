package core.parser;

import core.lexer.Token;
import core.lexer.TokenStream;
import core.lexer.TokenType;

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
			System.exit(1);
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
			System.exit(1);
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
			System.exit(1);
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
			System.exit(1);
			break;
		}
	}

	public void assignment(TreeNode node) {
		Token token = stream.token();
		if (token.isType(TokenType.ident)) {
			TreeNode assignNode = new TreeNode(token.toString(), token);
			node.addChild(assignNode);
			stream.next();

			token(TokenType.ASGN, node);
			if (stream.token().isType(TokenType.READINT)) {
				token(TokenType.READINT, node);
			} else {
				expression(node);
			}
		} else {
			System.err.println("PARSER ERROR: assignment; " + token);
			System.exit(1);
		}
	}

	public void ifStatement(TreeNode node) {
		Token token = stream.token();
		if (token.isType(TokenType.IF)) {
			TreeNode ifNode = new TreeNode(token.toString(), token);
			node.addChild(ifNode);
			stream.next();

			expression(node);
			token(TokenType.THEN, node);
			statementSequence(node);
			elseClause(node);
			token(TokenType.END, node);
		} else {
			System.err.println("PARSER ERROR: ifStatement; " + token);
			System.exit(1);
		}
	}

	public void elseClause(TreeNode node) {
		Token token = stream.token();
		if (token.isType(TokenType.ELSE)) {
			TreeNode elseNode = new TreeNode(token.toString(), token);
			node.addChild(elseNode);
			stream.next();

			statementSequence(node);
		} else {
			System.err.println("PARSER ERROR: elseClause; " + token);
			System.exit(1);
		}
	}

	public void whileStatement(TreeNode node) {
		Token token = stream.token();
		if (token.isType(TokenType.WHILE)) {
			TreeNode whileNode = new TreeNode(token.toString(), token);
			node.addChild(whileNode);
			stream.next();

			expression(node);
			token(TokenType.DO, node);
			statementSequence(node);
			token(TokenType.END, node);
		} else {
			System.err.println("PARSER ERROR: whileStatement; " + token);
			System.exit(1);
		}
	}

	public void writeInt(TreeNode node) {
		Token token = stream.token();
		if (token.isType(TokenType.WRITEINT)) {
			TreeNode writeIntNode = new TreeNode(token.toString(), token);
			node.addChild(writeIntNode);
			stream.next();

			expression(node);
		} else {
			System.err.println("PARSER ERROR: writeInt; " + token);
			System.exit(1);
		}
	}

	public void expression(TreeNode node) {
		simpleExpression(node);
		Token token = stream.token();
		if (token.isType(TokenType.COMPARE)) {
			token(TokenType.COMPARE, node);
			expression(node);
		}
	}

	public void simpleExpression(TreeNode node) {
		term(node);
		Token token = stream.token();
		if (token.isType(TokenType.ADDITIVE)) {
			token(TokenType.ADDITIVE, node);
			simpleExpression(node);
		}
	}

	public void term(TreeNode node) {
		factor(node);
		Token token = stream.token();
		if (token.isType(TokenType.MULTIPLICATIVE)) {
			token(TokenType.MULTIPLICATIVE, node);
			term(node);
		}
	}

	public void factor(TreeNode node) {
		Token token = stream.token();
		switch (token.getType()) {
		case ident:
		case num:
		case boollit:
			token(token.getType(), node);
			break;

		case LP:
			token(TokenType.LP, node);
			expression(node);
			token(TokenType.RP, node);
			break;

		default:
			System.err.println("PARSER ERROR: factor; " + token);
			System.exit(1);
			break;
		}
	}

}
