package earl.client.display;

import earl.client.data.Counter;
import earl.client.data.Hex;

public interface DisplayChangeListener {

	void counterMoved(Counter counter, Hex from, Hex to);

	void counterChanged(Counter piece);

	void counterDeselected(Counter selectedPiece);

	void counterSelected(Counter selectedPiece);

}
