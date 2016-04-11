package core.ast;

import java.util.ArrayList;
import java.util.List;

public class AssignmentStatement extends Statement {

	Identifier identifier;

	// one of these will be null
	Expression expression;
	ReadInt readint;

	@Override
	public List<ASTNode> getChildren() {
		List<ASTNode> children = new ArrayList<>();
		children.add(identifier);
		if (expression != null)
			children.add(expression);
		if (readint != null)
			children.add(readint);
		return children;
	}

	public AssignmentStatement() {
		super(StatementType.ASSIGNMENT);
	}

}
