package core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

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

		String tokFilename = sourceFile.getAbsolutePath().replaceAll("\\.tl$", ".tok");
		File tokFile = new File(tokFilename);

		// write the token stream to a file
		try {
			writeTokenStreamToFile(tokFile, lexer);
		} catch (IOException e) {
			System.err.println("Error writing to file: " + tokFile.getAbsolutePath());
			System.exit(1);
		}
		System.out.println("File created: " + tokFile.getAbsolutePath());

	}

	private static void writeTokenStreamToFile(File file, Lexer lexer) throws FileNotFoundException {
		PrintWriter w = new PrintWriter(file);
		Token token = lexer.getToken();
		while (token != null) {
			w.println(token.toString());
			token = lexer.getToken();
		}
		w.close();
	}

}
