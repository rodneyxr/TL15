package core.ast;

import java.util.Stack;

import core.lexer.Token;
import core.lexer.TokenStream;
import core.lexer.TokenType;

public class Parser2 {

	private TokenStream stream;
	private ASTNode ast;

	// Special Nodes
	private Declarations declarations;
	private Stack<StatementSequence> statementStack;

	public Parser2(TokenStream stream) {
		this.stream = stream;
	}

	public ASTNode getAST() {
		if (ast == null) {
			parse();
		}
		return ast;
	}

	public void parse() {
		declarations = new Declarations();
		statementStack = new Stack<>();
		try {
			ast = program();
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
	}

	public Token token(TokenType type) throws Exception {
		Token token = stream.token();
		if (token.isType(type)) {
			stream.next();
			return token;
		} else {
			throw new Exception("PARSER ERROR: " + stream.token() + " != " + type);
		}
	}

	public ASTNode program() throws Exception {
		Token token = stream.token();
		Program prog = null;
		if (token.isType(TokenType.PROGRAM)) {
			prog = new Program();
			token(TokenType.PROGRAM);
			prog.declarations = declarations();
			token(TokenType.BEGIN);
			statementStack.push(new StatementSequence());
			prog.statementSequence = statementSequence();
			statementStack.pop();
			return prog;
		} else {
			throw new Exception("PARSER ERROR: program; " + token);
		}
	}

	public Declarations declarations() throws Exception {
		Token token = stream.token();
		if (token.isType(TokenType.VAR)) {
			declarations.declarations.add(declaration());
		}
		return declarations;
	}

	public Declaration declaration() throws Exception {
		Declaration declaration = new Declaration();
		token(TokenType.VAR);
		declaration.ident = ident();
		token(TokenType.AS);
		declaration.type = type();
		token(TokenType.SC);
		declarations();
		return declaration;
	}

	public Identifier ident() throws Exception {
		Token token = token(TokenType.ident);
		Identifier ident = new Identifier();
		ident.varName = token.toString();
		return ident;
	}

	public DeclarationType type() throws Exception {
		Token token = stream.token();
		if (token.isType(TokenType.INT)) {
			token(TokenType.INT);
			return DeclarationType.INT;
		} else if (token.isType(TokenType.BOOL)) {
			token(TokenType.BOOL);
			return DeclarationType.BOOL;
		} else {
			throw new Exception("PARSER ERROR: type; " + token);
		}
	}

	public StatementSequence statementSequence() throws Exception {
		StatementSequence statList = statementStack.peek();
		Token token = stream.token();
		if (!token.isType(TokenType.END)) {
			statList.statements.add(statement());
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

	public AssignmentStatement assignment() throws Exception {
		AssignmentStatement assign = new AssignmentStatement();
		assign.identifier = ident();
		token(TokenType.ASGN);
		if (stream.token().isType(TokenType.READINT)) {
			token(TokenType.READINT);
			assign.readint = new ReadInt();
		} else {
			assign.expression = expression();
		}
		return assign;
	}

	public IfStatement ifStatement() throws Exception {
		IfStatement ifstat = new IfStatement();
		token(TokenType.IF);
		ifstat.expression = expression();
		token(TokenType.THEN);
		statementStack.push(new StatementSequence());
		ifstat.statements = statementSequence();
		statementStack.pop();
		ifstat.elseClause = elseClause();
		token(TokenType.END);
		return ifstat;
	}

	public ElseClause elseClause() throws Exception {
		Token token = stream.token();
		if (token.isType(TokenType.ELSE)) {
			token(TokenType.ELSE);
			ElseClause elseClause = new ElseClause();
			statementStack.push(new StatementSequence());
			elseClause.statements = statementSequence();
			statementStack.pop();
			return elseClause;
		}
		return null;
	}

	public WhileStatement whileStatement() throws Exception {
		WhileStatement whileStat = new WhileStatement();
		token(TokenType.WHILE);
		whileStat.expression = expression();
		token(TokenType.DO);
		statementStack.push(new StatementSequence());
		whileStat.statements = statementSequence();
		statementStack.pop();
		token(TokenType.END);
		return whileStat;
	}

	public WriteInt writeInt() throws Exception {
		WriteInt writeInt = new WriteInt();
		token(TokenType.WRITEINT);
		writeInt.expression = expression();
		return writeInt;
	}

	public Expression expression() throws Exception {
		Expression expr = new Expression();
		expr.left = simpleExpression();
		Token token = stream.token();
		if (token.isType(TokenType.COMPARE)) {
			token(TokenType.COMPARE);
			expr.right = expression();
		}
		return expr;
	}

	public SimpleExpression simpleExpression() throws Exception {
		SimpleExpression simpExpr = new SimpleExpression();
		simpExpr.term = term();
		Token token = stream.token();
		if (token.isType(TokenType.ADDITIVE)) {
			token(TokenType.ADDITIVE);
			simpExpr.simpleExpression = simpleExpression();
		}
		return simpExpr;
	}

	public Term term() throws Exception {
		Term term = new Term();
		term.factor = factor();
		Token token = stream.token();
		if (token.isType(TokenType.MULTIPLICATIVE)) {
			token(TokenType.MULTIPLICATIVE);
			term.term = term();
		}
		return term;
	}

	public Factor factor() throws Exception {
		Factor factor = new Factor();
		Token token = stream.token();
		switch (token.getType()) {
		case ident:
			factor.ident = ident();
			break;

		case num:
			factor.num = token(TokenType.num);
			break;

		case boollit:
			factor.boollit = token(TokenType.boollit);
			break;

		case LP:
			token(TokenType.LP);
			factor.expression = expression();
			token(TokenType.RP);
			break;

		default:
			throw new Exception("PARSER ERROR: factor; " + token);
		}

		return factor;
	}

}
