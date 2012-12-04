package earl.client.display.handler;

import earl.client.bastogne.op.JoinAttack;
import earl.client.bastogne.op.Move;
import earl.client.bastogne.op.PerformAttack;
import earl.client.data.Counter;
import earl.client.data.Hex;
import earl.client.display.OperationList;
import earl.client.games.BastogneSide;
import earl.client.games.SCSCounter;
import earl.client.op.EarlDisplay;

public class SCSDisplayHandler  {
	protected Counter selectedPiece = null;
	private final OperationList operations;
	private final EarlDisplay display;

	public SCSDisplayHandler(OperationList operations, EarlDisplay display) {
		this.operations = operations;
		this.display = display;
	}

	public void areaClicked(Hex area) {
		if (selectedPiece != null) {
			Move move = new Move();
			move.counter = selectedPiece;
			move.from = selectedPiece.getPosition();
			move.to = area;
			operations.add(move);
			setSelectedPiece(null);
		}
	}

	public void pieceClicked(SCSCounter piece) {
		if (piece == selectedPiece) {
			setSelectedPiece(null);
		} else {
			BastogneSide player=BastogneSide.US;//TODO
			if(piece.getOwner() == player) {
				setSelectedPiece(piece);
			}else{
				if(selectedPiece != null) {
					if(isAdjacent(piece, selectedPiece)) {
						JoinAttack join = new JoinAttack();
						join.from = selectedPiece.getPosition();
						join.target = piece.getPosition();
						operations.add(join);
					}
				}else{
					if(isAttacked(piece)) {
						PerformAttack attack = new PerformAttack();
						attack.target = piece.getPosition();
						operations.add(attack);
					}
				}
				setSelectedPiece(null);
			}
		}
}
	private boolean isAttacked(SCSCounter piece) {
		return true;
	}

	private boolean isAdjacent(Counter a, Counter b) {
		return true;
	}

	public void setSelectedPiece(Counter piece) {
		selectedPiece = piece;
		display.select(selectedPiece);
	}

	public Counter getSelectedPiece() {
		return selectedPiece;
	}
}
