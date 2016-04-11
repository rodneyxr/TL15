package core.ast;

public abstract class Statement extends ASTNode {

	StatementType type;
	Expression expression;

	public Statement(StatementType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return type.str;
	}
}
