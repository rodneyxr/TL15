package core.ast;

import java.util.ArrayList;
import java.util.List;

public class ElseClause extends ASTNode {

	private StatementSequence statements;

	public StatementSequence getStatements() {
		return statements;
	}

	public void setStatements(StatementSequence statements) {
		this.statements = statements;
		statements.setParent(this);
	}

	@Override
	public List<ASTNode> getChildren() {
		List<ASTNode> children = new ArrayList<>();
		children.add(statements);
		return children;
	}

	@Override
	public String toString() {
		return "else";
	}

}
