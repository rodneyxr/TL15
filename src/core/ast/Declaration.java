package core.ast;

import java.util.ArrayList;
import java.util.List;

public class Declaration extends ASTNode {

	private Identifier ident;
	private IdentifierType identType;
	
	public Identifier getIdent() {
		return ident;
	}

	public void setIdent(Identifier ident) {
		this.ident = ident;
		ident.setParent(this);
	}

	public IdentifierType getIdentType() {
		return identType;
	}

	public void setIdentType(IdentifierType identType) {
		this.identType = identType;
	}
	
	@Override
	public List<ASTNode> getChildren() {
		List<ASTNode> children = new ArrayList<>();
		return children;
	}

	@Override
	public String toString() {
		return String.format("decl:'%s':%s", ident.getVarName(), type);
	}

}
