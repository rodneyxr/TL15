package core.ast;

import java.util.ArrayList;
import java.util.List;

public class Expression extends ASTNode {

	private SimpleExpression left;
	
	// optional
	private Expression right;

	public SimpleExpression getLeft() {
		return left;
	}

	public void setLeft(SimpleExpression left) {
		this.left = left;
		left.setParent(this);
	}

	public Expression getRight() {
		return right;
	}

	public void setRight(Expression right) {
		this.right = right;
		right.setParent(this);
	}

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
