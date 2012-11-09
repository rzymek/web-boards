package earl.engine.client.data;

import earl.client.data.Counter;

public abstract class Counter extends Counter {
	public abstract void flip();

	public abstract boolean canFlip();

	@Override
	public boolean onClick() {
		if (canFlip()) {
			flip();
			return true;
		} else {
			return false;
		}
	}
}
