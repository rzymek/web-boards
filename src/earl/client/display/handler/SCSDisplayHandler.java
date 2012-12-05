package earl.client.display.handler;

import earl.client.bastogne.op.JoinAttack;
import earl.client.bastogne.op.PerformAttack;
import earl.client.data.Counter;
import earl.client.data.Hex;
import earl.client.games.BastogneSide;
import earl.client.games.SCSCounter;
import earl.client.op.EarlDisplay;
import earl.client.op.Operation;

public class SCSDisplayHandler extends BasicDisplayHandler {

	public SCSDisplayHandler(EarlDisplay display) {
		super(display);
	}

	@Override
	protected Operation moveToHex(Hex area) {
		if(area.getStack().isEmpty()) {
			return super.moveToHex(area);
		}else{
			SCSCounter counter = (SCSCounter) area.getStack().get(0);
			BastogneSide player = BastogneSide.US;//TODO
			if (counter.getOwner() == player) {
				setSelectedPiece(counter);
			} else {
				if (isAdjacent(counter, selectedPiece)) {
					JoinAttack join = new JoinAttack();
					join.from = selectedPiece.getPosition();
					join.target = counter.getPosition();
					setSelectedPiece(null);
					return join;
				}
			}			
		}
		return null;
	}

	@Override
	protected Operation startStackSelection(Hex area) {
		if (isAttacked(area)) {
			PerformAttack attack = new PerformAttack();
			attack.target = area;
			return attack;
		}else{
			return super.startStackSelection(area);
		}
	}
	
	private boolean isAttacked(Hex area) {
		return false;
	}

	private boolean isAdjacent(Counter a, Counter b) {
		return true;
	}

	@Override
	public void setSelectedPiece(Counter piece) {
		selectedPiece = piece;
		display.select(selectedPiece);
	}

	@Override
	public Counter getSelectedPiece() {
		return selectedPiece;
	}
}
