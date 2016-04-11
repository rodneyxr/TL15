package core.ast;

import java.util.ArrayList;
import java.util.List;

public class ElseClause extends ASTNode {

	StatementSequence statements;

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
