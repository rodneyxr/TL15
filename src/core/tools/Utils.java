package core.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import core.ast.ASTNode;
import core.lexer.TokenStream;
import core.parser.TreeNode;

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

	// Example:
	// digraph G {
	// ... 0 [label="CFG_ENTER"];
	// ... 1 [label="node 1"];
	// ... 2 [label="CFG_EXIT"];
	//
	// ... 0 -> 1;
	// ... 1 -> 2;
	// }
	public static String generateDOT(TreeNode root) {
		ArrayList<TreeNode> nodes = root.getAllNodes();

		StringBuilder dot = new StringBuilder("digraph G {\n");

		// first define all nodes
		for (TreeNode node : nodes) {
			dot.append("    ");
			dot.append(node.getID());
			dot.append("[label=\"");
			dot.append(node.toString().replaceAll("\"", "\\\""));
			dot.append("\"");

			if (node.getToken() != null)
				switch (node.getToken().getType()) {
				default:
					dot.append(",shape=box,fillcolor=gray,style=filled");
					break;
				}

			dot.append("];\n");
		}

		dot.append("\n");

		// wire all parents to their children
		for (TreeNode node : nodes) {
			for (TreeNode child : node.getChildren()) {
				// fp -> child
				dot.append("    ");
				dot.append(node.getID());
				dot.append(" -> ");
				dot.append(child.getID());
				dot.append(";\n");
			}
		}

		dot.append("}");
		return dot.toString();
	}

	public static String generateDOT(ASTNode root) {
		ArrayList<ASTNode> nodes = root.getAllNodes();

		StringBuilder dot = new StringBuilder("digraph G {\n");

		// first define all nodes
		for (ASTNode node : nodes) {
			dot.append("    ");
			dot.append(node.getID());
			dot.append("[label=\"");
			dot.append(node.toString().replaceAll("\"", "\\\""));
			dot.append("\"");

			dot.append(",shape=box");

			dot.append("];\n");
		}

		dot.append("\n");

		// wire all parents to their children
		for (ASTNode node : nodes) {
			for (ASTNode child : node.getChildren()) {
				// fp -> child
				dot.append("    ");
				dot.append(node.getID());
				dot.append(" -> ");
				dot.append(child.getID());
				dot.append(";\n");
			}
		}

		dot.append("}");
		return dot.toString();
	}

	// https://github.com/abstratt/eclipsegraphviz
	public static void saveDOTToFile(String dot, String filepath) {
		try (PrintStream ps = new PrintStream(filepath)) {
			ps.println(dot);
		} catch (FileNotFoundException e) {
			System.err.println(e);
		}
	}

}
