package webboards.client.display;

public class Color {
	public static final Color ORANGE = new Color("orange");
	public static final Color GREY = new Color("grey");
	public static final Color YELLOW = new Color("yellow");
	public static final Color RED = new Color("red");
	public static final Color MAGENTA = new Color("magenta");
 	private final String value;

	public Color(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}
