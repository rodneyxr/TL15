package core.ast;

import java.util.ArrayList;
import java.util.List;

public class IfStatement extends Statement {

	private StatementSequence statements;

	// may be null
	private ElseClause elseClause;

	public StatementSequence getStatements() {
		return statements;
	}

	public void setStatements(StatementSequence statements) {
		this.statements = statements;
		statements.setParent(this);
	}

	public ElseClause getElseClause() {
		return elseClause;
	}

	public void setElseClause(ElseClause elseClause) {
		this.elseClause = elseClause;
		elseClause.setParent(this);
	}

	public IfStatement() {
		super(StatementType.IFSTATMENT);
	}

	@Override
	public List<ASTNode> getChildren() {
		List<ASTNode> children = new ArrayList<>();
		children.add(getExpression());
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
