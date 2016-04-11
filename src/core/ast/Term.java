package core.ast;

import java.util.ArrayList;
import java.util.List;

public class Term extends SimpleExpression {

	Factor factor;
	
	// optional
	String multiplicative;
	Term term;

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
		if (multiplicative != null) {
			return multiplicative + ":int";
		}
		return "term";
	}

}
