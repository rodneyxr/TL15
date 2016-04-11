package core.ast;

import java.util.ArrayList;
import java.util.List;

public class WhileStatement extends Statement {

	private StatementSequence statements;

	public StatementSequence getStatements() {
		return statements;
	}

	public void setStatements(StatementSequence statements) {
		this.statements = statements;
		statements.setParent(this);
	}

	public WhileStatement() {
		super(StatementType.WHILESTATMENT);
	}

	@Override
	public List<ASTNode> getChildren() {
		List<ASTNode> children = new ArrayList<>();
		children.add(getExpression());
		children.add(statements);
		return children;
	}

	@Override
	public String toString() {
		return "while";
	}

}
