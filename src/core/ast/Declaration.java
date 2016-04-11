package core.ast;

import java.util.ArrayList;
import java.util.List;

public class Declaration extends ASTNode {

	DeclarationType type;
	Identifier ident;

	@Override
	public List<ASTNode> getChildren() {
		List<ASTNode> children = new ArrayList<>();
		return children;
	}

	@Override
	public String toString() {
		return String.format("decl:'%s':%s", ident, type);
	}

}
