package earl.client.display.handler;

import java.util.List;

import earl.client.bastogne.op.Move;
import earl.client.data.Counter;
import earl.client.data.Hex;
import earl.client.op.EarlDisplay;
import earl.client.op.Operation;

public class BasicDisplayHandler {
	protected Counter selectedPiece = null;
	private final EarlDisplay display;

	public BasicDisplayHandler(EarlDisplay display) {
		this.display = display;
	}

	public Operation areaClicked(Hex area) {
		if (selectedPiece == null) {
			List<Counter> stack = area.getStack();
			if(stack.isEmpty()) {
				//click on an empty hex
				//when no unit is selected
				return null;
			}
			setSelectedPiece(stack.get(stack.size() - 1));
		} else {
			if (selectedPiece.getPosition().equals(area)) {
				List<Counter> stack = area.getStack();
				int index = stack.indexOf(selectedPiece) - 1;
				if(index < 0) {
					display.alignStack(area);
					setSelectedPiece(null);
				}else{
					setSelectedPiece(stack.get(index));
				}
			}else{
				Move move = new Move();
				move.counter = selectedPiece;
				move.from = selectedPiece.getPosition();
				move.to = area;
				setSelectedPiece(null);
				return move;
			}
		}
		return null;
	}

	public void pieceClicked(Counter piece) {
		if (piece == selectedPiece) {
			setSelectedPiece(null);
		} else {
			if (selectedPiece == null) {
				setSelectedPiece(piece);
			} else {
				areaClicked(piece.getPosition());
			}
		}
	}

	public void setSelectedPiece(Counter piece) {
		selectedPiece = piece;
		display.select(selectedPiece);
	}

	public Counter getSelectedPiece() {
		return selectedPiece;
	}
}
