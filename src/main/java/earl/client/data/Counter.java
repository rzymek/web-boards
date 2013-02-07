package earl.client.data;

import earl.client.data.ref.CounterRef;
import earl.client.games.Ref;

public abstract class Counter extends Identifiable {
	private static final long serialVersionUID = 1L;
	protected Ref position = null;
	protected boolean flipped = false;

	public Ref getPosition() {
		return position;
	}

	public void setPosition(Ref pos) {
		this.position = pos;
	}

	public abstract String getState();

	public void flip() {
		flipped = !flipped;
	}

	public boolean isFlipped() {
		return flipped;
	}

	public CounterRef ref() {
		return new CounterRef(getId());
	}
	
	@Override
	public String toString() {
		return getId()+":"+getPosition().getSVGId();
	}

}
