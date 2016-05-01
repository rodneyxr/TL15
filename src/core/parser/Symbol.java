package core.parser;

import core.ast.IdentifierType;
import core.code.Register;

public class Symbol {

	String name;
	IdentifierType type;
	public Register reg;

	public Symbol(String name, IdentifierType type, Register reg) {
		this.name = name;
		this.type = type;
		this.reg = reg;
	}

}
