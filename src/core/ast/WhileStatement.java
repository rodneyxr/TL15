package core.ast;

import java.util.ArrayList;
import java.util.List;

public class WhileStatement extends Statement {

	Expression expression;
	StatementSequence statements;

	public WhileStatement() {
		super(StatementType.WHILESTATMENT);
	}

	@Override
	public List<ASTNode> getChildren() {
		List<ASTNode> children = new ArrayList<>();
		children.add(expression);
		children.add(statements);
		return children;
	}

	@Override
	public String toString() {
		return "while";
	}

}
