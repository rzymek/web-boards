package earl.client.games.scs.ops;

import java.util.ArrayList;
import java.util.List;

import earl.client.data.Board;
import earl.client.data.GameCtx;
import earl.client.data.HexInfo;
import earl.client.games.Hex;
import earl.client.games.scs.CombatResult;
import earl.client.games.scs.bastogne.Bastogne;
import earl.client.ops.Operation;
import earl.client.ops.ServerContext;
import earl.client.ops.generic.DiceRoll;

public class PerformAttack extends Operation {
	private static final long serialVersionUID = 1L;
	public Hex targetRef;
	public Hex[] attackingRef;
	public CombatResult result;
	public String rollResult;	
 
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
		return "Attack againts "+targetRef+": "+result+" ("+rollResult+")";
	}
}