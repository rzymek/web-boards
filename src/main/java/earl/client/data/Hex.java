package earl.client.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import earl.client.data.ref.HexId;
import earl.client.data.ref.HexRef;


public class Hex extends Identifiable {
	protected List<Counter> pieces = new ArrayList<Counter>();
	private String hexId = null;

	protected Hex() {
		hexId = "";
	}

	public Hex(String hexId) {
		this.hexId = hexId;
	}

	public List<Counter> getStack() {
		return Collections.unmodifiableList(pieces);
	}

	@Override
	public String getId() {
		return hexId;
	}

	@Override
	public String toString() {
		return hexId;
//		String s = super.toString() + " {";
//		for (Counter c : pieces) {
//			s += c.getId() + ", ";
//		}
//		return s + "}";
	}

	@Override
	public int hashCode() {
		return hexId.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Hex) {
			return hexId.equals(((Hex) obj).hexId);
		} else {
			return false;
		}
	}

	public HexRef ref() {
		return new HexId(hexId);
	}
}
