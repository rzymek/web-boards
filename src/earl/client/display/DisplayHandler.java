package earl.client.display;

import java.util.List;

import earl.client.data.Counter;
import earl.client.data.Hex;

public class DisplayHandler {
	protected Counter selectedPiece = null;
	protected Hex stackSelected = null;
	private final DisplayChangeListener listener;

	public DisplayHandler(DisplayChangeListener listener) {
		this.listener = listener;
	}

	public void areaClicked(Hex area) {
		hideStackSelection();
		if (selectedPiece != null) {
			Hex from = selectedPiece.getPosition();
			selectedPiece.setPosition(area);
			listener.counterMoved(selectedPiece, from, area);
			listener.alignStack(from);
			setSelectedPiece(null);
		}
	}

	public void pieceClicked(Counter piece) {
		Hex currentStack = stackSelected;
		hideStackSelection();
		if (piece == selectedPiece) {
			// click on an already selected piece -> piece special action
			piece.flip();
			listener.counterChanged(piece);
		} else {
			Hex hex = piece.getPosition();
			List<Counter> stack = hex.getStack();
			boolean selectedFromCurrentStack = (currentStack != null && currentStack.getId().equals(hex.getId()));
			boolean singleCounterSelected = (stack.size() == 1);
			boolean countersOverlap = listener.areCountersOverlapping(hex, stack);
			if(singleCounterSelected || selectedFromCurrentStack || !countersOverlap) {
				setSelectedPiece(piece);
			}else{
				setSelectedPiece(null);
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
