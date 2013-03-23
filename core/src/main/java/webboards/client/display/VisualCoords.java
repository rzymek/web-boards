package webboards.client.display;

public class VisualCoords {
	public int x;
	public int y;

	public VisualCoords(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public String toString() {
		return "("+x+", "+y+")";
	}	
}
