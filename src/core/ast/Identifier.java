package core.ast;

import java.util.ArrayList;
import java.util.List;

public class Identifier extends ASTNode {

	private String varName;

	public String getVarName() {
		return varName;
	}

	public void setVarName(String varName) {
		this.varName = varName;
	}

	@Override
	public List<ASTNode> getChildren() {
		List<ASTNode> children = new ArrayList<>();
		return children;
	}

	@Override
	public String toString() {
		return String.format("%s:%s", getVarName(), type);
	}

}
