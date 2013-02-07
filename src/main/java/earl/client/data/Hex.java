package earl.client.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import earl.client.ops.Operation;

public abstract class Hex extends Identifiable {
	private static final long serialVersionUID = 1L;
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
		return String.valueOf(pieces);
	}

	public abstract Operation onClicked();
}
