package core.parser;

import java.util.ArrayList;
import java.util.List;

import core.lexer.Token;

public class TreeNode {
	List<TreeNode> children = new ArrayList<TreeNode>();
	String nonterminal;
	Token token;
	
	public TreeNode(String nonterminal, Token token) {
		this.nonterminal = nonterminal;
		this.token = token;
	}
	
	public void addChild(TreeNode node) {
		children.add(node);
	}
}
