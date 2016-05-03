package core.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import core.ast.ASTNode;
import core.cfg.FlowPoint;
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

	public static String writeCodeToFile(File sourceFile, String code) {
		File file = Utils.changeTinyExt(sourceFile, ".s");
		try {
			PrintWriter w = new PrintWriter(file);
			w.print(code);
			w.close();
		} catch (FileNotFoundException e) {
			System.err.println("Error writing to file: " + file.getAbsolutePath());
			System.exit(1);
		}
		return file.getAbsolutePath();
	}

	public static File changeTinyExt(File sourceFile, String extention) {
		return new File(sourceFile.getAbsolutePath().replaceAll("\\.tl$", extention));
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
	public static String generateDOT(ASTNode root) {
		ArrayList<ASTNode> nodes = root.getAllNodes();

		StringBuilder dot = new StringBuilder("digraph G {\n");
		dot.append("    node[shape=box,style=filled,fillcolor=\"white\"];\n");

		// first define all nodes
		for (ASTNode node : nodes) {
			dot.append("    ");
			dot.append(node.getID());
			dot.append("[label=\"");
			dot.append(node.toString().replaceAll("\"", "\\\""));
			dot.append("\"");

			dot.append(",shape=box");
			if (node.typeError)
				dot.append(",fillcolor=\"/pastel13/1\"");

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
	
	public static String generateCFGDOT(FlowPoint cfg) {
		ArrayList<FlowPoint> flowpoints = cfg.getAllFlowPoints();
		System.out.println("Utils.java: " + flowpoints.size());

		StringBuilder dot = new StringBuilder("digraph G {\n");
		dot.append("    node[shape=box,style=filled,fillcolor=\"white\"];\n");

		// first define all nodes
		for (FlowPoint fp : flowpoints) {
			dot.append("    ");
			dot.append(fp.getID());
			dot.append("[label=\"");
			dot.append(fp.toString().replaceAll("\"", "\\\""));
			dot.append("\"");

			dot.append(",shape=box");

			dot.append("];\n");
		}

		dot.append("\n");

		// wire all parents to their children
		for (FlowPoint fp : flowpoints) {
			for (FlowPoint child : fp.getChildren()) {
				// fp -> child
				dot.append("    ");
				dot.append(fp.getID());
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
