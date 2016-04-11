package core.ast;

import java.util.ArrayList;
import java.util.List;

public class Identifier extends ASTNode {

	String varName;
	// IdentifierType type;

	public IdentifierType getType() {
		return Parser2.symbolTable.get(varName);
	}

	@Override
	public List<ASTNode> getChildren() {
		List<ASTNode> children = new ArrayList<>();
		return children;
	}

	@Override
	public String toString() {
		return String.format("%s:%s", varName, getType());
	}

}
