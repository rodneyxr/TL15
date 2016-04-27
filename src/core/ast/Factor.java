package core.ast;

import java.util.ArrayList;
import java.util.List;

import core.lexer.Token;

public class Factor extends Term {

	// 3/4 of these will be null
	private Identifier ident;
	private Token num;
	private Token boollit;
	private Expression expression;

	public Identifier getIdent() {
		return ident;
	}

	public void setIdent(Identifier ident) {
		this.ident = ident;
	}

	public Token getNum() {
		return num;
	}

	public void setNum(Token num) {
		this.num = num;
	}

	public Token getBoollit() {
		return boollit;
	}

	public void setBoollit(Token boollit) {
		this.boollit = boollit;
	}

	public Expression getExpression() {
		return expression;
	}

	public void setExpression(Expression expression) {
		this.expression = expression;
	}

	@Override
	public List<ASTNode> getChildren() {
		List<ASTNode> children = new ArrayList<>();
		if (getExpression() != null)
			children.add(getExpression());
		return children;
	}

	@Override
	public String toString() {
		if (getIdent() != null)
			return getIdent().toString();
		if (getNum() != null)
			return getNum().getText() + ":int";
		if (getBoollit() != null)
			return getBoollit().getText() + ":int";
		return "factor";
	}

}
