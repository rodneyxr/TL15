package core.code;

public class Label {

	private static int ifCount = 0;

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

	@Override
	public String toString() {
		return text;
	}

}
