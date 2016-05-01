package core.code;

public class Register {

	String var;
	int offset;

	private static int globalOffset = 4;

	public Register(String var) {
		this.var = var;
	}
	
	public static Register next() {
		Register register = new Register("$fp");
		Register.globalOffset -= 4;
		register.offset = Register.globalOffset;
		return register;
	}
	
	@Override
	public String toString() {
		if (var.equals("$fp"))
			return offset + "($fp)";
		return var;
	}

}
