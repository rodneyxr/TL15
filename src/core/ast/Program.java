package core.ast;

import java.util.ArrayList;
import java.util.List;

public class Program extends ASTNode {

	Declarations declarations;
	StatementSequence statementSequence;

	@Override
	public List<ASTNode> getChildren() {
		List<ASTNode> list = new ArrayList<>();
		list.add(declarations);
		list.add(statementSequence);
		return list;
	}

	@Override
	public String toString() {
		return "program";
	}

}
