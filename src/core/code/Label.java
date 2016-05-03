package core.code;

public class Label {

	private static int ifCount = 0;
	private static int whileCount = 0;

	private String text;

	public Label(String text) {
		this.text = text;
	}

	public static Label[] nextIfLabels() {
		Label[] labels = { //
				new Label("Else" + ifCount), // else label
				new Label("EndIf" + ifCount) // end if label
		};
		ifCount++;
		return labels;
	}
	
	public static Label[] nextWhileLabels() {
		Label[] labels = { //
				new Label("WhileStart" + whileCount), // while start label
				new Label("WhileEnd" + whileCount) // while end label
		};
		whileCount++;
		return labels;
	}

	@Override
	public String toString() {
		return text;
	}

}
