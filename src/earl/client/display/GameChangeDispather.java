package earl.client.display;

import java.util.ArrayList;
import java.util.List;

import earl.client.data.Counter;
import earl.client.data.Hex;

public class GameChangeDispather implements GameChangeListener {
	private List<GameChangeListener> listeners = new ArrayList<GameChangeListener>();

	@Override
	public void counterMoved(Counter counter, Hex from, Hex to) {
		for (GameChangeListener l : listeners) {
			l.counterMoved(counter, from, to);
		}
	}

	@Override
	public void counterChanged(Counter piece) {
		for (GameChangeListener l : listeners) {
			l.counterChanged(piece);
		}
	}

	public void add(GameChangeListener gameListener) {
		listeners.add(gameListener);
	}

}
