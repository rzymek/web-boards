package earl.client.display;

import java.util.List;

import earl.client.data.Counter;
import earl.client.data.Hex;

public class DisplayHandler {
	protected Counter selectedPiece = null;
	protected Hex stackSelected = null;
	private final GameChangeDispather listeners = new GameChangeDispather();
	private final DisplayChangeListener display;

	public DisplayHandler(DisplayChangeListener display) {
		this.display = display;
		this.listeners.add(display);
	}

	public void areaClicked(Hex area) {
		hideStackSelection();
		if (selectedPiece != null) {
			Hex from = selectedPiece.getPosition();
			selectedPiece.setPosition(area);
			listeners.counterMoved(selectedPiece, from, area);
			display.alignStack(from);
			setSelectedPiece(null);
		}
	}

	public void pieceClicked(Counter piece) {
		Hex currentStack = stackSelected;
		hideStackSelection();
		if (piece == selectedPiece) {
			// click on an already selected piece -> piece special action
			piece.flip();
			listeners.counterFlipped(piece);
		} else {
			Hex hex = piece.getPosition();
			List<Counter> stack = hex.getStack();
			boolean selectedFromCurrentStack = (currentStack != null && currentStack.getId().equals(hex.getId()));
			boolean singleCounterSelected = (stack.size() == 1);
			boolean countersOverlap = display.areCountersOverlapping(hex, stack);
			if(singleCounterSelected || selectedFromCurrentStack || !countersOverlap) {
				setSelectedPiece(piece);
			}else{
				setSelectedPiece(null);
				display.showStackSelection(hex);
				stackSelected = hex;
			}
		}
	}

	private void hideStackSelection() {
		if(stackSelected != null) {
			display.alignStack(stackSelected);
			stackSelected = null;
		}
	}

	protected void setSelectedPiece(Counter piece) {
		if (selectedPiece != null) {
			display.counterDeselected(selectedPiece);
		}
		selectedPiece = piece;
		if (selectedPiece != null) {
			display.counterSelected(selectedPiece);
		}
	}

	public void addGameListener(GameChangeListener listener){
		listeners.add(listener);
	}
}
