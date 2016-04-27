package core.ast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class ASTNode {
	
	private static final AtomicInteger ID_GENERATOR = new AtomicInteger();

	protected boolean visited = false; // used for graph traversal

	private int id; // unique ID to be used when creating Graphviz DOT file
	private ASTNode parent;
	
	public ASTNode() {
		id = ID_GENERATOR.getAndIncrement();
	}

	public abstract List<ASTNode> getChildren();

	@Override
	public abstract String toString();

	/**
	 * Gets the unique ID of this node.
	 * 
	 * @return The ID of this node.
	 */
	public int getID() {
		return id;
	}

	public ArrayList<ASTNode> getAllNodes() {
		ArrayList<ASTNode> childs = new ArrayList<>();
		getAllNodesImpl(childs);
		resetVisited();
		return childs;
	}

	/**
	 * Implementation of getAllNodes(). This private method is used to hide the
	 * parameter that is used by the recursion.
	 * 
	 * @param childs
	 *            The list that holds all nodes.
	 * @return The list that holds all nodes.
	 */
	private ArrayList<ASTNode> getAllNodesImpl(ArrayList<ASTNode> childs) {
		if (visited)
			return childs;
		childs.add(this);
		visited = true;

		for (ASTNode child : getChildren()) {
			child.getAllNodesImpl(childs);
		}

		return childs;
	}

	/*
	 * Sets the boolean printed to false for all nodes under this instance.
	 */
	public void resetVisited() {
		visited = false;
		for (ASTNode child : getChildren()) {
			if (!visited)
				continue;
			child.resetVisited();
		}
	}

	protected void setParent(ASTNode parent) {
		this.parent = parent;
	}

	public void reduce() {
		List<ASTNode> children = getChildren();
		if (parent != null) {
			if (children.size() == 1) {
				if (this instanceof Term) {
					Term term = (Term) this;
					if (parent instanceof Term) {
						Term parentTerm = (Term) parent;
						parentTerm.setTerm(term.getFactor());
					} else if (parent instanceof SimpleExpression) {
						SimpleExpression simpexpr = (SimpleExpression) parent;
						simpexpr.setTerm(term.getFactor());
					}
				} 
				else if (this instanceof SimpleExpression) {
					SimpleExpression simple = (SimpleExpression) this;
					if (parent instanceof SimpleExpression) {
						SimpleExpression parentSimple = (SimpleExpression) parent;
						parentSimple.setSimpleExpression(simple.getTerm().getFactor());
					}
					else if (parent instanceof Expression) {
						Expression parentExpr = (Expression) parent;
						Term term = simple.getTerm();
						if (term.getChildren().size() > 1)
							parentExpr.setLeft(simple.getTerm());
						else
							parentExpr.setLeft(simple.getTerm().getFactor());
					}
					else if (parent instanceof Statement) {
						Statement parentStat = (Statement) parent;
						parentStat.setExpression(simple.getTerm().getFactor());
					}
				}
				else if (this instanceof Expression) {
					Expression expr = (Expression) this;
					if (parent instanceof Expression) {
						Expression parentExpr = (Expression) parent;
						parentExpr.setRight(expr.getLeft().getTerm().getFactor());
					}
					else if (parent instanceof Statement) {
						Statement parentStat = (Statement) parent;
						if (expr.getLeft().getTerm().getChildren().size() > 1)
							parentStat.setExpression(expr.getLeft().getTerm());
						else
							parentStat.setExpression(expr.getLeft());
					}
				}
			}
		}

		parent = this;
		for (ASTNode child : getChildren()) {
			child.reduce();
		}
	}

}
