package core.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import core.lexer.Token;

public class TreeNode {

	private static final AtomicInteger ID_GENERATOR = new AtomicInteger();
	private int id; // unique ID to be used when creating Graphviz DOT file
	private boolean visited = false; // used for graph traversal

	List<TreeNode> children = new ArrayList<>();
	String nonterminal;
	Token token;

	public TreeNode(String nonterminal, Token token) {
		this.nonterminal = nonterminal;
		this.token = token;
		id = ID_GENERATOR.getAndIncrement();
	}

	/**
	 * Gets the unique ID of this node.
	 * 
	 * @return The ID of this node.
	 */
	public int getID() {
		return id;
	}

	public void addChild(TreeNode node) {
		children.add(node);
	}

	public String getNonTerminal() {
		return nonterminal;
	}

	public Token getToken() {
		return token;
	}

	public List<TreeNode> getChildren() {
		return children;
	}

	/**
	 * Get all nodes under this node. The list returned will also include
	 * itself.
	 * 
	 * @return A list of all nodes.
	 */
	public ArrayList<TreeNode> getAllNodes() {
		ArrayList<TreeNode> childs = new ArrayList<>();
		getAllNodesImpl(childs);
		resetVisited();
		return childs;
	}

	@Override
	public String toString() {
		if (token == null)
			return nonterminal;
		return token.toString();
	}

	/**
	 * Implementation of getAllNodes(). This private method is used to hide the
	 * parameter that is used by the recursion.
	 * 
	 * @param childs
	 *            The list that holds all nodes.
	 * @return The list that holds all nodes.
	 */
	private ArrayList<TreeNode> getAllNodesImpl(ArrayList<TreeNode> childs) {
		if (visited)
			return childs;
		childs.add(this);
		visited = true;

		for (TreeNode child : children) {
			child.getAllNodesImpl(childs);
		}

		return childs;
	}

	/*
	 * Sets the boolean printed to false for all nodes under this instance.
	 */
	private void resetVisited() {
		visited = false;
		for (TreeNode child : children) {
			if (!visited)
				continue;
			child.resetVisited();
		}
	}
}
