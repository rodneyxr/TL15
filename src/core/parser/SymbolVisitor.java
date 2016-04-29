package core.parser;

import java.util.HashMap;

import core.ast.Declaration;

public class SymbolVisitor extends BaseVisitor {

	HashMap<String, Symbol> table;

	public SymbolVisitor() {
		table = new HashMap<>();
	}
	
	public HashMap<String, Symbol> getSymbolTable() {
		return table;
	}
	
	@Override
	public void visit(Declaration declaration) {
		final String name = declaration.getIdent().getVarName();
		if (table.get(name) == null) {
			table.put(name, new Symbol(name, declaration.getIdentType()));
		} else {
			System.err.println("PARSER ERROR: Double declaration");
		}
	}

}
