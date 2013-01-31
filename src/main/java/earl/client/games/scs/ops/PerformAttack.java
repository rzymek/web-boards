package earl.client.games.scs.ops;

import java.util.ArrayList;
import java.util.List;

import earl.client.data.Board;
import earl.client.data.Hex;
import earl.client.data.ref.HexRef;
import earl.client.display.EarlDisplay;
import earl.client.games.scs.CombatResult;
import earl.client.games.scs.bastogne.Bastogne;
import earl.client.ops.Operation;
import earl.client.ops.ServerContext;
import earl.client.ops.generic.DiceRoll;

public class PerformAttack extends Operation {
	private static final long serialVersionUID = 1L;
	public HexRef targetRef;
	public HexRef[] attackingRef;
	public CombatResult result;
	public String rollResult;	
 
	@Override
	public void draw(Board board, EarlDisplay g) {
	}
	@Override
	public void clientExecute(Board board) {
	}

	@Override
	public void serverExecute(ServerContext ctx) {
		Bastogne game = (Bastogne) ctx.game;
		Board board = game.getBoard();
		Hex target = board.get(targetRef);
		List<Hex> attacking = getAttacking(board);
		int[] odds = game.calculateOdds(target, attacking);
		DiceRoll roll = new DiceRoll();
		roll.dice = 2;
		roll.sides = 6;
		roll.serverExecute(ctx);	
		int sum = roll.getSum();
		rollResult = String.valueOf(sum);
		result = game.getCombatResult(odds, sum);
	}
	private List<Hex> getAttacking(Board board) {
		List<Hex> attacking = new ArrayList<Hex>(attackingRef.length);
		for (HexRef ref : attackingRef) {
			attacking.add(board.get(ref));
		}
		return attacking;
	}

	@Override
	public void postServer(Board board, EarlDisplay display) {
		Hex target = board.get(targetRef);
		display.clearOds(display.getCenter(target));
		display.showResults(display.getCenter(target), result.toString());
	}
	
	@Override
	public String toString() {
		return "Attack againts "+targetRef+": "+result+" ("+rollResult+")";
	}
}