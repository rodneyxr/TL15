package core.ast;

import java.util.ArrayList;
import java.util.List;

public class Assignment extends Statement {

	private Identifier identifier;

	// one of these will be null
	// Expression expression;
	ReadInt readint;
	
	public Identifier getIdentifier() {
		return identifier;
	}
	
	public void setIdentifier(Identifier identifier) {
		this.identifier = identifier;
		identifier.setParent(this);
	}
	
	public ReadInt getReadInt() {
		return readint;
	}
	
	public void setReadInt(ReadInt readint) {
		this.readint = readint;
		readint.setParent(this);
	}

	@Override
	public List<ASTNode> getChildren() {
		List<ASTNode> children = new ArrayList<>();
		children.add(identifier);
		if (expression != null)
			children.add(expression);
		if (readint != null)
			children.add(readint);
		return children;
	}

	public Assignment() {
		super(StatementType.ASSIGNMENT);
	}

}
