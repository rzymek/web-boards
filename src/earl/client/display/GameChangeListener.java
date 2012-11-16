package earl.client.display;

import earl.client.data.Counter;
import earl.client.data.Hex;

public interface GameChangeListener {

	void counterMoved(Counter counter, Hex from, Hex to);

	void counterFlipped(Counter piece);

}
