package core.ast;

import java.util.ArrayList;
import java.util.List;

import core.lexer.Token;

public class SimpleExpression extends Expression {

	private Term term;

	// optional
	private Token additive;
	private SimpleExpression simpleExpression;

	public Term getTerm() {
		return term;
	}

	public void setTerm(Term term) {
		this.term = term;
		term.setParent(this);
	}

	public SimpleExpression getSimpleExpression() {
		return simpleExpression;
	}

	public void setSimpleExpression(SimpleExpression simpleExpression) {
		this.simpleExpression = simpleExpression;
		simpleExpression.setParent(this);
	}
	
	public Token getAdditive() {
		return additive;
	}

	public void setAdditive(Token additive) {
		this.additive = additive;
	}

	@Override
	public List<ASTNode> getChildren() {
		List<ASTNode> children = new ArrayList<>();
		children.add(term);
		if (simpleExpression != null)
			children.add(simpleExpression);
		return children;
	}

	@Override
	public String toString() {
		if (getAdditive() != null) {
			return getAdditive().getText() + ":" + type;
		}
		return "simpexpr";
	}

}
