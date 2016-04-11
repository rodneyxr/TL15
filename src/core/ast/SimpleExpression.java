package core.ast;

import java.util.ArrayList;
import java.util.List;

public class SimpleExpression extends ASTNode {

	Term term;
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
		// TODO implement this
		return "simpexpr";
	}

}
