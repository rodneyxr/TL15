package core.ast;

import java.util.ArrayList;
import java.util.List;

public class Declarations extends ASTNode {

	private List<Declaration> declarations = new ArrayList<>();

	public void addDeclaration(Declaration declaration) {
		declarations.add(declaration);
		declaration.setParent(this);
	}
	
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
