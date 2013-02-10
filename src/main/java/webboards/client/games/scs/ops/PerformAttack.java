package webboards.client.games.scs.ops;

import java.util.Collection;

import webboards.client.data.Board;
import webboards.client.data.GameCtx;
import webboards.client.games.Hex;
import webboards.client.games.scs.CombatResult;
import webboards.client.games.scs.SCSBoard;
import webboards.client.games.scs.SCSCounter;
import webboards.client.games.scs.SCSHex;
import webboards.client.games.scs.bastogne.Bastogne;
import webboards.client.ops.Operation;
import webboards.client.ops.ServerContext;
import webboards.client.ops.generic.DiceRoll;

public class PerformAttack extends Operation {
	private static final long serialVersionUID = 1L;
	public Hex targetRef;
	public Hex[] attackingRef;
	public CombatResult result;
	public String rollResult;

	@SuppressWarnings("unused")
	private PerformAttack() {
	}

	public PerformAttack(Hex target, Collection<Hex> attacking) {
		this.targetRef = target;
		this.attackingRef = attacking.toArray(new Hex[attacking.size()]);
	}

	@Override
	public void drawDetails(GameCtx ctx) {
		for (Hex from : attackingRef) {
			ctx.display.drawArrow(from, targetRef, "combat_"+from.getSVGId());
		}
	}

	@Override
	public void updateBoard(Board board) {
	}

	@Override
	public void serverExecute(ServerContext ctx) {
		Bastogne game = (Bastogne) ctx.game;
		SCSBoard board = (SCSBoard) game.getBoard();
		SCSHex target = (SCSHex) board.getInfo(targetRef);
		int[] odds = SCSCounter.calculateOdds(target, board.getAttackingInfo(targetRef), targetRef);
		DiceRoll roll = new DiceRoll();
		roll.dice = 2;
		roll.sides = 6;
		roll.serverExecute(ctx);
		int sum = roll.getSum();
		rollResult = String.valueOf(sum);
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