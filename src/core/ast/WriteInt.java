package core.ast;

import java.util.ArrayList;
import java.util.List;

public class WriteInt extends Statement {

	public WriteInt() {
		super(StatementType.WRITEINT);
	}

	@Override
	public List<ASTNode> getChildren() {
		List<ASTNode> children = new ArrayList<>();
		children.add(getExpression());
		return children;
	}

	@Override
	public String toString() {
		return "writeint";
	}

}
