package core.ast;

public abstract class Statement extends ASTNode {

	StatementType statementType;
	private Expression expression;

	public Expression getExpression() {
		return expression;
	}

	public void setExpression(Expression expression) {
		this.expression = expression;
		expression.setParent(this);
	}

	public Statement(StatementType statementType) {
		this.statementType = statementType;
	}

	@Override
	public String toString() {
		return statementType.str;
	}
}
