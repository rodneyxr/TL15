package core;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import core.ast.Program;
import core.code.CodeVisitor;
import core.lexer.Lexer;
import core.lexer.TokenStream;
import core.parser.Parser;
import core.parser.ParserException;
import core.parser.SymbolVisitor;
import core.parser.TypeVisitor;
import core.tools.Utils;

public class Main {

	public static void main(String[] args) {
		// validate the arguments
		if (args.length != 1) {
			System.err.println("Usage: prog <basename>.tl");
			System.exit(1);
		}

		if (!args[0].endsWith(".tl")) {
			System.err.println("Error: Source file must end with '.tl'");
			System.exit(1);
		}

		// create the file
		File sourceFile = new File(args[0]);

		// create the scanner
		Scanner scanner = null;
		try {
			scanner = new Scanner(sourceFile);
		} catch (FileNotFoundException e) {
			System.err.println("File not found: " + sourceFile.getName());
			System.exit(1);
		}

		// create the lexer
		Lexer lexer = new Lexer(scanner);
		TokenStream stream = new TokenStream(lexer);
		Parser parser = new Parser(stream);
		Program ast = parser.parse();

		SymbolVisitor symbolVisitor = null;
		TypeVisitor typeVisitor = null;
		CodeVisitor codeVisitor;

		// create the symbol table
		try {
			symbolVisitor = new SymbolVisitor();
			symbolVisitor.visit(ast);
		} catch (ParserException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}

		// handle type checking
		try {
			typeVisitor = new TypeVisitor(symbolVisitor.getSymbolTable());
			typeVisitor.visit(ast);
		} catch (Exception e) {
		}

		String dot = Utils.generateDOT(parser.getAST());
		String astFilePath = sourceFile.getAbsolutePath().replaceFirst("\\.tl$", ".ast.dot");
		Utils.saveDOTToFile(dot, astFilePath);
		System.out.println("AST DOT file written to: " + astFilePath);

		if (typeVisitor.hasTypeError()) {
			System.out.println("Program had errors.");
			return;
		}

		try {
			codeVisitor = new CodeVisitor(symbolVisitor.getSymbolTable());
			codeVisitor.visit(ast);
			String mipsFilePath = Utils.writeCodeToFile(sourceFile, codeVisitor.getCode());
			System.out.println("MIPS Assembly file written to: " + mipsFilePath);

			System.out.println("Compiled Successfully!");
		} catch (ParserException e) {
			System.err.println(e.getMessage());
		}

	}

}
