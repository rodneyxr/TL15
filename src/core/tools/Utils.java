package core.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import core.lexer.TokenStream;

public class Utils {

	public static void writeTokenStreamToFile(File file, TokenStream stream) {
		try {
			PrintWriter w = new PrintWriter(file);
			w.print(stream);
			w.close();
		} catch (FileNotFoundException e) {
			System.err.println("Error writing to file: " + file.getAbsolutePath());
			System.exit(1);
		}
	}

	public static String changeTinyExtention(File sourceFile, String extention) {
		return sourceFile.getAbsolutePath().replaceAll("\\.tl$", extention);
	}

}
