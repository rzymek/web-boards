package earl.client.display;

import earl.client.data.Counter;
import earl.client.data.Hex;

public class DisplayHandler {
	protected Counter selectedPiece;
	private final DisplayChangeListener listener;

	public DisplayHandler(DisplayChangeListener listener) {
		this.listener = listener;
	}

	protected void areaClicked(Hex area) {
		if (selectedPiece != null) {
			Hex from = selectedPiece.getPosition();
			selectedPiece.setPosition(area);
			listener.counterMoved(selectedPiece, from, area);
		}
	}

	protected void pieceClicked(Counter piece) {
		if (piece == selectedPiece) {
			// click on an already selected piece -> piece special action
			piece.flip();
			listener.counterChanged(piece);
		} else {
			setSelectedPiece(piece);
		}

	}

	protected void setSelectedPiece(Counter piece) {
		if (selectedPiece != null) {
			listener.counterDeselected(selectedPiece);
		}
		selectedPiece = piece;
		if (selectedPiece != null) {
			listener.counterSelected(selectedPiece);
		}
	}

}
