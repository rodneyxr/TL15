package core.ast;

public abstract class Statement extends ASTNode {

	StatementType type;
	private Expression expression;

	public Expression getExpression() {
		return expression;
	}

	public void setExpression(Expression expression) {
		this.expression = expression;
		expression.setParent(this);
	}

	public Statement(StatementType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return type.str;
	}
}
