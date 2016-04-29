package core.parser;

import core.ast.IdentifierType;

public class Symbol {

	String name;
	IdentifierType type;
	Regisiter register;

	public Symbol(String name, IdentifierType type) {
		this.name = name;
		this.type = type;
	}

}
