package core.ast;

import java.util.ArrayList;
import java.util.List;

import core.lexer.Token;

public class Expression extends ASTNode {

	private SimpleExpression left;

	// optional
	private Expression right;
	private Token compare;

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
	
	public Token getCompare() {
		return compare;
	}

	public void setCompare(Token compare) {
		this.compare = compare;
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
		if (getCompare() != null)
			return getCompare().getText() + ":" + type;
		return "expression";
	}

}
