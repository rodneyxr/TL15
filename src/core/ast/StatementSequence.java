package core.ast;

import java.util.ArrayList;
import java.util.List;

public class StatementSequence extends ASTNode {

	public List<Statement> statements = new ArrayList<>();
	
	public void addStatement(Statement statement) {
		statements.add(statement);
		statement.setParent(this);
	}

	@Override
	public List<ASTNode> getChildren() {
		List<ASTNode> children = new ArrayList<>();
		children.addAll(statements);
		return children;
	}

	@Override
	public String toString() {
		return "stmt list:" + type;
	}

}
