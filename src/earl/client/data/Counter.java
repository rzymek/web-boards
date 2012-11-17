package earl.client.data;


public abstract class Counter extends Identifiable {
	protected Hex position = null;
	protected boolean flipped = false;

	public Hex getPosition() {
		return position;
	}

	public void setPosition(Hex to) {
		Hex from = position;
		if (from != null) {
			from.pieces.remove(this);
		}
		position = to;
		if (!to.pieces.contains(this)) {
			to.pieces.add(this);
		}
	}

	public abstract String getState();

	public void flip() {
		flipped = !flipped;
	}

	public boolean isFlipped() {
		return flipped;
	}
}
