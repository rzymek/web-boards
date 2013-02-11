package webboards.client.games.scs.ops;

import webboards.client.data.GameCtx;
import webboards.client.data.ref.CounterId;
import webboards.client.games.Hex;
import webboards.client.games.scs.SCSBoard;
import webboards.client.games.scs.SCSCounter;
import webboards.client.games.scs.SCSHex;
import webboards.client.games.scs.SCSMarker;
import webboards.client.games.scs.bastogne.Bastogne;
import webboards.client.games.scs.bastogne.BastogneSide;
import webboards.client.ops.Operation;
import webboards.client.ops.ServerContext;
import webboards.client.ops.generic.DiceRoll;

public class PerformBarrage extends Operation {
	private static final long serialVersionUID = 1L;
	private CounterId arty;
	private Hex target;
	private boolean resultDG = false;
	private int diceRoll;

	@SuppressWarnings("unused")
	private PerformBarrage() {
	}

	public boolean isResultDG() {
		return resultDG;
	}
	
	public PerformBarrage(SCSCounter arty, Hex target) {
		this.arty = arty.ref();
		this.target = target;
	}

	@Override
	public void serverExecute(ServerContext ctx) {
		Bastogne game = (Bastogne) ctx.game;
		SCSBoard board = (SCSBoard) game.getBoard();
		SCSHex hex = board.getInfo(target);
		int mod = hex.getBarrageModifier();
		SCSCounter attacker = (SCSCounter) board.getInfo(arty);
		int attack = attacker.getAttack() + mod;
		
		DiceRoll roll = new DiceRoll();
		roll.dice = 1;
		roll.sides = 6;
		roll.serverExecute(ctx);
		diceRoll = roll.getSum();
		
		resultDG = (diceRoll <= attack);
	}
	
	@Override
	public void postServer(GameCtx ctx) {
		if(resultDG) {
			SCSMarker dg = new SCSMarker("dg"+target, "admin/misc_us-dg.png", BastogneSide.US);
			ctx.board.place(target, dg);
			ctx.display.createCounter(dg, ctx.board);
			ctx.display.alignStack(target);
		}
		
		ctx.display.clearOds(arty.toString());
//		ctx.display.showResults(ctx.display.getCenter(target), "Hit");
	}

	@Override
	public String toString() {
		return "Barrage on " + target + " by " + arty + ": " + (resultDG ? "DG" : "Ineffective") + " ("+diceRoll+")";
	}

}
