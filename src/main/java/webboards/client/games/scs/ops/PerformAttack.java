package webboards.client.games.scs.ops;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import webboards.client.data.Board;
import webboards.client.data.GameCtx;
import webboards.client.data.HexInfo;
import webboards.client.games.Hex;
import webboards.client.games.scs.CombatResult;
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
		Board board = game.getBoard();
		HexInfo target = board.getInfo(targetRef);
		List<HexInfo> attacking = getAttacking(board);
		int[] odds = game.calculateOdds(target, attacking);
		DiceRoll roll = new DiceRoll();
		roll.dice = 2;
		roll.sides = 6;
		roll.serverExecute(ctx);
		int sum = roll.getSum();
		rollResult = String.valueOf(sum);
		result = game.getCombatResult(odds, sum);
	}

	private List<HexInfo> getAttacking(Board board) {
		List<HexInfo> attacking = new ArrayList<HexInfo>(attackingRef.length);
		for (Hex ref : attackingRef) {
			attacking.add(board.getInfo(ref));
		}
		return attacking;
	}

	@Override
	public void postServer(GameCtx ctx) {
		ctx.display.clearOds(ctx.display.getCenter(targetRef));
		ctx.display.showResults(ctx.display.getCenter(targetRef), result.toString());
	}

	@Override
	public String toString() {
		return "Attack againts " + targetRef + ": " + result + " (" + rollResult + ")";
	}
}