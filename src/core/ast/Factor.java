package core.ast;

import java.util.ArrayList;
import java.util.List;

import core.lexer.Token;

public class Factor extends ASTNode {

	// 3/4 of these will be null
	Identifier ident;
	Token num;
	Token boollit;
	Expression expression;

	@Override
	public List<ASTNode> getChildren() {
		List<ASTNode> children = new ArrayList<>();
		if (expression != null)
			children.add(expression);
		return children;
	}

	@Override
	public String toString() {
		if (ident != null)
			return ident.toString();
		if (num != null)
			return num.toString();
		if (boollit != null)
			return boollit.toString();
		return "factor";
	}

}
