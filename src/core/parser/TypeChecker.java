package core.parser;

import java.util.HashMap;

import core.ast.Declaration;

public class TypeChecker extends BaseVisitor {

	HashMap<String, Symbol> symbolTable = new HashMap<>();

	@Override
	public void visit(Declaration declaration) {
		System.out.println(declaration);
	}

}
