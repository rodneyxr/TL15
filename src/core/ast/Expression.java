package core.ast;

import java.util.ArrayList;
import java.util.List;

public class Expression extends ASTNode {

	SimpleExpression left;
	Expression right;

	@Override
	public List<ASTNode> getChildren() {
		List<ASTNode> children = new ArrayList<>();
		children.add(left);
		if (right != null)
			children.add(right);
		return children;
	}

	@Override
	public String toString() {
		// TODO implement this
		return "expression";
	}

}
