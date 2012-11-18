package earl.client.display;

import earl.client.data.Counter;
import earl.client.data.Hex;

public interface GameHandler {

	void areaClicked(Hex area);

	void pieceClicked(Counter piece);

	void setSelectedPiece(Counter piece);

}
