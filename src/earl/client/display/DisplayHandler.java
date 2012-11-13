package earl.client.display;

import java.util.Collection;

import earl.client.data.Counter;
import earl.client.data.Hex;

public class DisplayHandler {
	protected Counter selectedPiece = null;
	protected Hex stackSelected = null;
	private final DisplayChangeListener listener;

	public DisplayHandler(DisplayChangeListener listener) {
		this.listener = listener;
	}

	protected void areaClicked(Hex area) {
		hideStackSelection();
		if (selectedPiece != null) {
			Hex from = selectedPiece.getPosition();
			selectedPiece.setPosition(area);
			listener.counterMoved(selectedPiece, from, area);
			listener.alignStack(from);
			setSelectedPiece(null);
		}
	}

	protected void pieceClicked(Counter piece) {
		Hex currentStack = stackSelected;
		hideStackSelection();
		if (piece == selectedPiece) {
			// click on an already selected piece -> piece special action
			piece.flip();
			listener.counterChanged(piece);
		} else {
			Hex hex = piece.getPosition();
			Collection<Counter> stack = hex.getStack();
			boolean selectedFromCurrentStack = (currentStack != null && currentStack.getId().equals(hex.getId()));
			boolean singleCounterSelected = (stack.size() == 1);
			if(singleCounterSelected || selectedFromCurrentStack) {
				setSelectedPiece(piece);
			}else{
				listener.showStackSelection(hex);
				stackSelected = hex;
			}
		}
	}

	private void hideStackSelection() {
		if(stackSelected != null) {
			listener.alignStack(stackSelected);
			stackSelected = null;
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
