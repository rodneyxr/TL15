package core.parser;

import java.util.HashMap;

import core.ast.Declaration;
import core.code.Register;

public class SymbolVisitor extends BaseVisitor {

	HashMap<String, Symbol> table;

	public SymbolVisitor() {
		table = new HashMap<>();
	}

	public HashMap<String, Symbol> getSymbolTable() {
		return table;
	}

	@Override
	public void visit(Declaration declaration) throws ParserException {
		final String name = declaration.getIdent().getVarName();
		if (table.get(name) == null) {
			table.put(name, new Symbol(name, declaration.getIdentType(), Register.next()));
		} else {
			throw new ParserException("PARSER ERROR: Double declaration; " + name + " already defined.");
		}
	}

}
