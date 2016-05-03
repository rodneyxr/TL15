package core.cfg;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import core.code.Instruction;

public class FlowPoint {

	private static final AtomicInteger ID_GENERATOR = new AtomicInteger();
	private int id; // unique ID to be used when creating Graphviz DOT file
	private boolean visited = false; // used for graph traversal

	private List<FlowPoint> children;
	private List<Instruction> instructions;

	public FlowPoint() {
		id = ID_GENERATOR.getAndIncrement();
		children = new ArrayList<>();
		instructions = new ArrayList<>();
	}

	public int getID() {
		return id;
	}

	public boolean isEmpty() {
		return instructions.isEmpty();
	}

	public List<FlowPoint> getChildren() {
		return children;
	}

	/**
	 * Get all flow points under this flow point. The list returned will also
	 * include itself.
	 * 
	 * @return A list of all flow points.
	 */
	public ArrayList<FlowPoint> getAllFlowPoints() {
		ArrayList<FlowPoint> children = new ArrayList<FlowPoint>();
		getAllFlowPointsImpl(children);
		resetVisited();
		return children;
	}

	public void addFlowPoint(FlowPoint child) {
		children.add(child);
	}

	public void addInstruction(Instruction instruction) {
		instructions.add(instruction);
	}

	/**
	 * Implementation of getAllFlowPoints(). This private method is used to hide
	 * the parameter that is used by the recursion.
	 * 
	 * @param children
	 *            The list that holds all flow points.
	 * @return The list that holds all flow points.
	 */
	private ArrayList<FlowPoint> getAllFlowPointsImpl(ArrayList<FlowPoint> children) {
		if (visited)
			return children;

		children.add(this);
		visited = true;
		for (FlowPoint child : this.children) {
			child.getAllFlowPointsImpl(children);
		}

		return children;
	}

	/*
	 * Sets the boolean visited to false for all flow points under this instance
	 * of flow point.
	 */
	private void resetVisited() {
		visited = false;
		for (FlowPoint child : this.children) {
			if (!visited)
				continue;
			child.resetVisited();
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
//		sb.append(instructions.get(0));
		for (Instruction i : instructions) {
			sb.append(i);
			sb.append('\n');
		}
		return sb.toString();
	}

}
