package core.ast;

import java.util.ArrayList;
import java.util.List;

public class SimpleExpression extends Expression {

	Term term;

	// optional
	String additive;
	SimpleExpression simpleExpression;

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
		if (additive != null) {
			return additive + ":int";
		}
		return "simpexpr";
	}

}
