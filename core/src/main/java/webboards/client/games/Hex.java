package webboards.client.games;

import webboards.client.utils.Utils;


public class Hex implements Position {
	private static final long serialVersionUID = 1L;
	public int x;
	public int y;

	public Hex() {
	}

	public Hex(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String getSVGId() {
		StringBuilder id = new StringBuilder("h");
		d02(id, x);
		d02(id, y);
		return id.toString();
	}

	private void d02(StringBuilder id, int i) {
		if (i < 10)
			id.append('0');
		id.append(i);
	}

	public static Hex fromSVGId(String id) {
		int x = Integer.parseInt(id.substring(1, 3));
		int y = Integer.parseInt(id.substring(3, 5));
		return new Hex(x, y);
	}

	@Override
	public String toString() {
		StringBuilder id = new StringBuilder();
		d02(id, x);
		id.append('.');
		d02(id, y);
		return id.toString();
	}
	@Override
	public int hashCode() {
		return getSVGId().hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		return Utils.equals(this, obj);
	}
}
