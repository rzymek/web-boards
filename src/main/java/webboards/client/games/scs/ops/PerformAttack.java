package webboards.client.games.scs.ops;

import java.util.Collection;

import webboards.client.data.Board;
import webboards.client.data.GameCtx;
import webboards.client.games.Hex;
import webboards.client.games.scs.CombatResult;
import webboards.client.games.scs.SCSBoard;
import webboards.client.games.scs.SCSColor;
import webboards.client.games.scs.SCSCounter;
import webboards.client.games.scs.SCSHex;
import webboards.client.games.scs.bastogne.Bastogne;
import webboards.client.ops.AbstractOperation;
import webboards.client.ops.ServerContext;
import webboards.client.ops.generic.DiceRoll;

public class PerformAttack extends AbstractOperation {
	private static final long serialVersionUID = 1L;
	public Hex targetRef;
	public Collection<Hex> attackingRef;
	public CombatResult result;
	public String rollResult;

	@SuppressWarnings("unused")
	private PerformAttack() {
	}

	public PerformAttack(Hex target, Collection<Hex> attacking) {
		this.targetRef = target;
		this.attackingRef = attacking;
	}

	@Override
	public void drawDetails(GameCtx ctx) {
		for (Hex from : attackingRef) {
			SCSColor color;
			if (result.A > 0 && result.D > 0) {
				color = SCSColor.PARTIAL_SUCCESS;
			} else if (result.A > 0 && result.D <= 0) {
				color = SCSColor.FAILURE;
			} else {
				color = SCSColor.SUCCESS;
			}
			String id = "combat_" + from.getSVGId();
			ctx.display.drawArrow(from, targetRef, id, color.getColor());
		}
	}

	@Override
	public void updateBoard(Board board) {
	}

	@Override
	public void serverExecute(ServerContext ctx) {
		Bastogne game = (Bastogne) ctx.game;
		SCSBoard board = (SCSBoard) game.getBoard();
		SCSHex target = board.getInfo(targetRef);
		Collection<SCSHex> attacking = board.getInfo(attackingRef);
		int[] odds = SCSCounter.calculateOdds(target, attacking, targetRef);
		DiceRoll roll = new DiceRoll();
		roll.dice = 2;
		roll.sides = 6;
		roll.serverExecute(ctx);
		int sum = roll.getSum();
		rollResult = String.valueOf(sum) + " - " + odds[0] + ":" + odds[1];
		result = game.getCombatResult(odds, sum);
	}

	@Override
	public void postServer(GameCtx ctx) {
		ctx.display.clearOds(targetRef.getSVGId());
		ctx.display.showResults(ctx.display.getCenter(targetRef), result.toString());
	}

	@Override
	public String toString() {
		return "Attack againts " + targetRef + ": " + result + " (" + rollResult + ")";
	}
}