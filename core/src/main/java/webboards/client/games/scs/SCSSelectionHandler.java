package webboards.client.games.scs;

import java.util.Collection;
import java.util.List;

import webboards.client.OpRunner;
import webboards.client.data.CounterInfo;
import webboards.client.data.GameCtx;
import webboards.client.display.SelectionHandler;
import webboards.client.games.Hex;
import webboards.client.games.Position;
import webboards.client.games.scs.ops.AcknowlegeResults;
import webboards.client.games.scs.ops.PerformAttack;
import webboards.client.games.scs.ops.PerformBarrage;
import webboards.client.ops.Operation;

public class SCSSelectionHandler extends SelectionHandler {

	public SCSSelectionHandler(GameCtx ctx, OpRunner runner) {
		super(ctx, runner);
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
	protected Operation onSingleCounterClicked(CounterInfo counter) {
		Operation op = arrackOrAcknowlege(counter.getPosition());
		return op != null ? op : super.onSingleCounterClicked(counter);
	}

	@Override 
	protected Operation onStackClicked(List<CounterInfo> stack, Position pos) {
		Operation op = arrackOrAcknowlege(pos);
		return op != null ? op : super.onStackClicked(stack, pos);
	}

	private Operation arrackOrAcknowlege(Position pos) {
		if(ctx.selected != null) {
			return null;
		}
		SCSBoard board = (SCSBoard) ctx.board;
		if (!(pos instanceof Hex)) {
			return null;
		}
		Hex target = (Hex) pos;
		CombatResult combatResult = board.combatResultsShown.get(target);
		if (combatResult != null) {
			return new AcknowlegeResults(target, combatResult);
		}
		Collection<SCSCounter> declaredBarrages = board.getBarragesOn(target);
		if (!declaredBarrages.isEmpty()) {
			SCSCounter attacking = declaredBarrages.iterator().next();
			return new PerformBarrage(attacking, target);
		}
		if (board.isDeclaredAttackOn(target)) {
			// TODO: issue #5 PerformAttack countdown
			Collection<Hex> attacking = board.getAttacking(target);
			return new PerformAttack(target, attacking);
		}
		return null;
	}
}
