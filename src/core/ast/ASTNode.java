package core.ast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class ASTNode {

	private static final AtomicInteger ID_GENERATOR = new AtomicInteger();
	private int id; // unique ID to be used when creating Graphviz DOT file
	protected boolean visited = false; // used for graph traversal

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

	private ASTNode parent;
	protected void setParent(ASTNode parent) {
		this.parent = parent;
	}

	public void clean() {
		List<ASTNode> children = getChildren();
		if (parent != null) {
			if (children.size() == 1) {
				if (this instanceof Term) {
					Term term = (Term) this;
					if (parent instanceof Term) {
						Term parentTerm = (Term) parent;
						System.out.println(parentTerm + " -> " + term);
						parentTerm.term = term.factor;
					} else if (parent instanceof SimpleExpression) {
						SimpleExpression simpexpr = (SimpleExpression) parent;
						simpexpr.term = term.factor;
					}
				}
				// if (parent instanceof Statement) {
				// Statement stmt = (Statement) parent;
				// ASTNode child = children.get(0);
				// if (child instanceof SimpleExpression) {
				// stmt.expression = ((SimpleExpression) child).term;
				// }
				// } else if (parent instanceof SimpleExpression) {
				// System.out.println(this);
				// SimpleExpression simpexpr = (SimpleExpression) parent;
				// ASTNode child = children.get(0);
				// if (child instanceof Term) {
				// simpexpr.term = ((Term) child).factor;
				// }
				// }
			}
		}

		parent = this;
		for (ASTNode child : getChildren()) {
			child.clean();
		}
	}

}
