package earl.client.data;

import java.io.Serializable;


public abstract class Counter implements Identifiable, Serializable {
	protected Hex position = null;

	public Hex getPosition() {
		return position;
	}

	public void setPosition(Hex to) {
		Hex from = position;
		if (from != null) {
			from.pieces.remove(this);
		}
		position = to;
		to.pieces.add(this);
	}

	@Override
	public abstract String getId();
	
	public abstract String getState();

	public void flip() {
	}
}
