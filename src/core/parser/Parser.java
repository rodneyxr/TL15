package core.parser;

import java.util.Collections;
import java.util.Stack;

import core.ast.Assignment;
import core.ast.Declaration;
import core.ast.Declarations;
import core.ast.ElseClause;
import core.ast.Expression;
import core.ast.Factor;
import core.ast.Identifier;
import core.ast.IdentifierType;
import core.ast.IfStatement;
import core.ast.Program;
import core.ast.ReadInt;
import core.ast.SimpleExpression;
import core.ast.Statement;
import core.ast.StatementSequence;
import core.ast.Term;
import core.ast.WhileStatement;
import core.ast.WriteInt;
import core.lexer.Token;
import core.lexer.TokenStream;
import core.lexer.TokenType;

public class Parser {

	private TokenStream stream;
	private Program ast;

	// Special Nodes
	private Declarations declarations;
	private Stack<StatementSequence> statementStack;

	public Parser(TokenStream stream) {
		this.stream = stream;
	}

	public Program getAST() {
		if (ast == null) {
			parse();
		}
		return ast;
	}

	public Program parse() {
		declarations = new Declarations();
		statementStack = new Stack<>();
		try {
			ast = program();
			ast.reduce();
			Collections.reverse(declarations.declarations);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
		return ast;
	}

	public Token token(TokenType type) throws Exception {
		Token token = stream.token();
		if (token == null)
			throw new Exception("PARSER ERROR: reached end of file.");
		if (token.isType(type)) {
			stream.next();
			return token;
		} else {
			throw new Exception("PARSER ERROR: " + stream.token() + " != " + type);
		}
	}

	public Program program() throws Exception {
		Token token = stream.token();
		Program prog = null;
		if (token.isType(TokenType.PROGRAM)) {
			prog = new Program();
			token(TokenType.PROGRAM);
			prog.setDeclarations(declarations());
			token(TokenType.BEGIN);
			statementStack.push(new StatementSequence());
			prog.setStatementSequence(statementSequence());
			statementStack.pop();
			return prog;
		} else {
			throw new Exception("PARSER ERROR: program; " + token);
		}
	}

	public Declarations declarations() throws Exception {
		Token token = stream.token();
		if (token.isType(TokenType.VAR)) {
			declarations.addDeclaration(declaration());
		}
		return declarations;
	}

	public Declaration declaration() throws Exception {
		Declaration declaration = new Declaration();
		token(TokenType.VAR);
		declaration.setIdent(ident());
		token(TokenType.AS);
		declaration.setIdentType(type());
		token(TokenType.SC);
		declarations();
		return declaration;
	}

	public Identifier ident() throws Exception {
		Token token = token(TokenType.ident);
		Identifier ident = new Identifier();
		ident.setVarName(token.getText());
		return ident;
	}

	public IdentifierType type() throws Exception {
		Token token = stream.token();
		if (token.isType(TokenType.INT)) {
			token(TokenType.INT);
			return IdentifierType.INT;
		} else if (token.isType(TokenType.BOOL)) {
			token(TokenType.BOOL);
			return IdentifierType.BOOL;
		} else {
			throw new Exception("PARSER ERROR: type; " + token);
		}
	}

	public StatementSequence statementSequence() throws Exception {
		StatementSequence statList = statementStack.peek();
		Token token = stream.token();
		if (!token.isType(TokenType.END) && !token.isType(TokenType.ELSE)) {
			statList.addStatement(statement());
			token(TokenType.SC);
			statementSequence();
		}
		return statList;
	}

	public Statement statement() throws Exception {
		Token token = stream.token();
		switch (token.getType()) {
		case ident:
			return assignment();
		case IF:
			return ifStatement();
		case WHILE:
			return whileStatement();
		case WRITEINT:
			return writeInt();
		default:
			throw new Exception("PARSER ERROR: statement; " + token);
		}
	}

	public Assignment assignment() throws Exception {
		Assignment assign = new Assignment();
		assign.setIdentifier(ident());
		token(TokenType.ASGN);
		if (stream.token().isType(TokenType.READINT)) {
			token(TokenType.READINT);
			assign.setReadInt(new ReadInt());
		} else {
			assign.setExpression(expression());
		}
		return assign;
	}

	public IfStatement ifStatement() throws Exception {
		IfStatement ifstat = new IfStatement();
		token(TokenType.IF);
		ifstat.setExpression(expression());
		token(TokenType.THEN);
		statementStack.push(new StatementSequence());
		ifstat.setStatements(statementSequence());
		statementStack.pop();
		ElseClause elseClause = elseClause();
		if (elseClause != null)
			ifstat.setElseClause(elseClause);
		token(TokenType.END);
		return ifstat;
	}

	public ElseClause elseClause() throws Exception {
		Token token = stream.token();
		if (token.isType(TokenType.ELSE)) {
			token(TokenType.ELSE);
			ElseClause elseClause = new ElseClause();
			statementStack.push(new StatementSequence());
			elseClause.setStatements(statementSequence());
			statementStack.pop();
			return elseClause;
		}
		return null;
	}

	public WhileStatement whileStatement() throws Exception {
		WhileStatement whileStat = new WhileStatement();
		token(TokenType.WHILE);
		whileStat.setExpression(expression());
		token(TokenType.DO);
		statementStack.push(new StatementSequence());
		whileStat.setStatements(statementSequence());
		statementStack.pop();
		token(TokenType.END);
		return whileStat;
	}

	public WriteInt writeInt() throws Exception {
		WriteInt writeInt = new WriteInt();
		token(TokenType.WRITEINT);
		writeInt.setExpression(expression());
		return writeInt;
	}

	public Expression expression() throws Exception {
		Expression expr = new Expression();
		expr.setLeft(simpleExpression());
		Token token = stream.token();
		if (token.isType(TokenType.COMPARE)) {
			expr.setCompare(token(TokenType.COMPARE));
			expr.setRight(expression());
		}
		return expr;
	}

	public SimpleExpression simpleExpression() throws Exception {
		SimpleExpression simpExpr = new SimpleExpression();
		simpExpr.setTerm(term());
		Token token = stream.token();
		if (token.isType(TokenType.ADDITIVE)) {
			simpExpr.setAdditive(token(TokenType.ADDITIVE));
			simpExpr.setSimpleExpression(simpleExpression());
		}
		return simpExpr;
	}

	public Term term() throws Exception {
		Term term = new Term();
		term.setFactor(factor());
		Token token = stream.token();
		if (token.isType(TokenType.MULTIPLICATIVE)) {
			term.setMultiplicative(token.getText());
			token(TokenType.MULTIPLICATIVE);
			term.setTerm(term());
		}
		return term;
	}

	public Factor factor() throws Exception {
		Factor factor = new Factor();
		Token token = stream.token();
		switch (token.getType()) {
		case ident:
			factor.setIdent(ident());
			break;

		case num:
			factor.setNum(token(TokenType.num));
			break;

		case boollit:
			factor.setBoollit(token(TokenType.boollit));
			break;

		case LP:
			token(TokenType.LP);
			factor.setExpression(expression());
			token(TokenType.RP);
			break;

		default:
			throw new Exception("PARSER ERROR: factor; " + token);
		}

		return factor;
	}

}
