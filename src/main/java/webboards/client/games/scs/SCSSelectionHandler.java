package webboards.client.games.scs;

import java.util.Collection;

import webboards.client.data.CounterInfo;
import webboards.client.data.GameCtx;
import webboards.client.display.SelectionHandler;
import webboards.client.games.Hex;
import webboards.client.games.Position;
import webboards.client.games.scs.ops.PerformAttack;
import webboards.client.ops.Operation;

public class SCSSelectionHandler extends SelectionHandler {

	public SCSSelectionHandler(GameCtx ctx) {
		super(ctx);
	}

	@Override
	public boolean canSelect(CounterInfo counter) {
		if (counter instanceof SCSCounter) {
			SCSCounter c = (SCSCounter) counter;
			return (ctx.side == c.getOwner());
		}
		return true;
	}

	@Override
	public Operation onSelected(CounterInfo counter) {
		if (counter == null) {
			return null;
		}
		SCSBoard board = (SCSBoard) ctx.board;
		Position pos = counter.getPosition();
		if(!(pos instanceof Hex)) {
			return null;
		}
		Hex target = (Hex) pos;
		Collection<Hex> attacking = board.getAttacking(target);
		if (board.isDeclaredAttackOn(target)) {
			// TODO: issue #5 PerformAttack countdown
			board.clearAttacksOn(target);
			return new PerformAttack(target, attacking);
		} else {
			return null;
		}
	}
}
