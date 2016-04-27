package core;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import core.lexer.Lexer;
import core.lexer.TokenStream;
import core.parser.Parser;
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

		parser.parse();
		System.out.println("Compiled Successfully!");

		String dot = Utils.generateDOT(parser.getAST());
		String astFilePath = sourceFile.getAbsolutePath().replaceFirst("\\.tl$", ".ast.dot");
		Utils.saveDOTToFile(dot, astFilePath);
		System.out.println("AST DOT file written to: " + astFilePath);
	}

}
