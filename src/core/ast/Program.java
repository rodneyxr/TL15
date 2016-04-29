package core.ast;

import java.util.ArrayList;
import java.util.List;

public class Program extends ASTNode {

	private Declarations declarations;
	private StatementSequence statementSequence;

	public Declarations getDeclarations() {
		return declarations;
	}

	public void setDeclarations(Declarations declarations) {
		this.declarations = declarations;
		declarations.setParent(this);
	}

	public StatementSequence getStatementSequence() {
		return statementSequence;
	}

	public void setStatementSequence(StatementSequence statementSequence) {
		this.statementSequence = statementSequence;
		statementSequence.setParent(this);
	}

	@Override
	public List<ASTNode> getChildren() {
		List<ASTNode> list = new ArrayList<>();
		list.add(declarations);
		list.add(statementSequence);
		return list;
	}

	@Override
	public String toString() {
		return "program:" + type;
	}

}
