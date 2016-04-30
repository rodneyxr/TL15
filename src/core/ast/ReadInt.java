package core.ast;

import java.util.ArrayList;
import java.util.List;

public class ReadInt extends ASTNode {
	
	@Override
	public List<ASTNode> getChildren() {
		List<ASTNode> children = new ArrayList<>();
		return children;
	}

	@Override
	public String toString() {
		return "readint:" + type;
	}

}
