package core.ast;

public abstract class Statement extends ASTNode {

	StatementType type;

	public Statement(StatementType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return type.str;
	}
}
