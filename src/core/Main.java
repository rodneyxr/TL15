package core;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Usage: prog <basename>.tl");
			System.exit(1);
		}

		File file = new File(args[0]);
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			System.err.println("File not found: " + file.getName());
			System.exit(1);
		}

		Lexer lexer = new Lexer(scanner);
		Token token = lexer.getToken();
		while (token != null) {
			System.out.println(token);
			token = lexer.getToken();
		}

	}

}
