package earl.client.data;

import earl.client.data.ref.Counter;
import earl.client.games.Position;

public abstract class CounterInfo extends Identifiable {
	private static final long serialVersionUID = 1L;
	protected Position position = null;
	protected boolean flipped = false;

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position pos) {
		this.position = pos;
	}

	public abstract String getState();

	public void flip() {
		flipped = !flipped;
	}

	public boolean isFlipped() {
		return flipped;
	}

	public Counter ref() {
		return new Counter(getId());
	}
	
	@Override
	public String toString() {
		return getId()+":"+getPosition().getSVGId();
	}

}
