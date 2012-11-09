package earl.engine.client;

import earl.client.data.Hex;
import earl.client.data.Counter;

public class Display {
	protected Counter selectedPiece;

	protected void areaClicked(Hex area) {
		if (selectedPiece != null) {
			Hex from = selectedPiece.getPosition();
			selectedPiece.setPosition(area);
			pieceMoved(selectedPiece, from, area);
		}
	}

	protected void pieceClicked(Counter piece) {
		if (piece == selectedPiece) {
			// click on an already selected piece -> piece special action
			piece.onClick();
			pieceAction(piece);
		} else {
			setSelectedPiece(piece);
		}

	}

	protected void setSelectedPiece(Counter piece) {		
		selectedPiece = piece;		
	}
}
