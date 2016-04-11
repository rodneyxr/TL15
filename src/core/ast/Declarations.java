package core.ast;

import java.util.ArrayList;
import java.util.List;

public class Declarations extends ASTNode {

	List<Declaration> declarations = new ArrayList<>();

	@Override
	public List<ASTNode> getChildren() {
		List<ASTNode> children = new ArrayList<>();
		children.addAll(declarations);
		return children;
	}

	@Override
	public String toString() {
		return "decl list";
	}

}
