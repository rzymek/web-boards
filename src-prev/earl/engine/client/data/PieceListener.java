package earl.engine.client.data;

import earl.client.data.Hex;
import earl.client.data.Counter;

public interface PieceListener {
	void pieceMoved(Counter piece, Hex from, Hex to);
}
