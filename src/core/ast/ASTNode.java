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
	private void resetVisited() {
		visited = false;
		for (ASTNode child : getChildren()) {
			if (!visited)
				continue;
			child.resetVisited();
		}
	}

}