package core.ast;

import java.util.ArrayList;
import java.util.List;

public class Term extends ASTNode {

	Factor factor;
	Term term; // optional

	@Override
	public List<ASTNode> getChildren() {
		List<ASTNode> children = new ArrayList<>();
		children.add(factor);
		if (term != null)
			children.add(term);
		return children;
	}

	@Override
	public String toString() {
		// TODO: implement this
		return "term";
	}

}
