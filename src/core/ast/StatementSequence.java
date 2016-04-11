package core.ast;

import java.util.ArrayList;
import java.util.List;

public class StatementSequence extends ASTNode {

	List<Statement> statements = new ArrayList<>();

	@Override
	public List<ASTNode> getChildren() {
		List<ASTNode> children = new ArrayList<>();
		children.addAll(statements);
		return children;
	}

	@Override
	public String toString() {
		return "stmt list";
	}

}
