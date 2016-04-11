package core.ast;

import java.util.ArrayList;
import java.util.List;

public class IfStatement extends Statement {

	Expression expression;
	StatementSequence statements;

	// may be null
	ElseClause elseClause;

	public IfStatement() {
		super(StatementType.IFSTATMENT);
	}

	@Override
	public List<ASTNode> getChildren() {
		List<ASTNode> children = new ArrayList<>();
		children.add(expression);
		children.add(statements);
		if (elseClause != null)
			children.add(elseClause);
		return children;
	}

	@Override
	public String toString() {
		return "if";
	}

}
