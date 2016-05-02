package core.ast;

import java.util.ArrayList;
import java.util.List;

public class Term extends SimpleExpression {

	private Factor factor;

	// optional
	private String multiplicative;
	private Term term;

	public Factor getFactor() {
		return factor;
	}

	public void setFactor(Factor factor) {
		this.factor = factor;
		factor.setParent(factor);
	}

	public Term getTerm() {
		return term;
	}

	public void setTerm(Term term) {
		this.term = term;
		term.setParent(this);
	}

	public String getMultiplicative() {
		return multiplicative;
	}

	public void setMultiplicative(String multiplicative) {
		this.multiplicative = multiplicative;
	}

	@Override
	public List<ASTNode> getChildren() {
		List<ASTNode> children = new ArrayList<>();
		children.add(factor);
		if (term != null)
			children.add(term);
		return children;
	}

	@Override
	public String toString() {
		if (getMultiplicative() != null) {
			return getMultiplicative() + ":" + type;
		}
		return "term";
	}

}
