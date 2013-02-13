package webboards.client.games.scs.ops;

import webboards.client.data.GameCtx;
import webboards.client.data.ref.CounterId;
import webboards.client.display.VisualCoords;
import webboards.client.ex.EarlException;
import webboards.client.games.Hex;
import webboards.client.games.scs.SCSBoard;
import webboards.client.games.scs.SCSColor;
import webboards.client.games.scs.SCSCounter;
import webboards.client.games.scs.SCSHex;
import webboards.client.games.scs.SCSMarker;
import webboards.client.games.scs.bastogne.ArtyType;
import webboards.client.games.scs.bastogne.Bastogne;
import webboards.client.games.scs.bastogne.BastogneSide;
import webboards.client.ops.AbstractOperation;
import webboards.client.ops.ServerContext;
import webboards.client.ops.generic.DiceRoll;

public class PerformBarrage extends AbstractOperation {
	private static final long serialVersionUID = 1L;
	private CounterId arty;
	private Hex target;
	private boolean resultDG = false;
	private int killSteps = 0;
	private int diceRoll = -1;
	private int killRoll = -1;

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
	public void drawDetails(GameCtx ctx) {
		SCSCounter a = (SCSCounter) ctx.board.getInfo(arty);
		SCSColor color;
		if (diceRoll < 0) {
			// before dice rolls
			color = SCSColor.DELCARE;
		} else {
			if (resultDG) {
				if (killSteps > 0)
					color = SCSColor.SUCCESS;
				else
					color = SCSColor.PARTIAL_SUCCESS;
			} else {
				color = SCSColor.FAILURE;
			}
		}
		String id = "barrage_" + a.getPosition().getSVGId();
		ctx.display.drawArrow(a.getPosition(), target, id, color.getColor());
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
		if (resultDG) {
			roll.serverExecute(ctx);
			killRoll = roll.getSum();
			int value = getKillRollValue(attacker);
			if (killRoll >= value)
				killSteps = 1;
			else
				killSteps = 0;
		} else {
			killRoll = 0;
			killSteps = 0;
		}

	}

	private int getKillRollValue(SCSCounter attacker) {
		ArtyType artyType = attacker.getArtyType();
		if(artyType == null) {
			throw new EarlException(attacker.ref()+" is missing artyType");
		}
		switch (artyType) {
		case YELLOW:  return 4;
		case GUNS_88: return 5;
		case OTHER:   return 6;		
		default:
			throw new EarlException("Unsupported ArtyType:"+artyType);
		}
	}

	@Override
	public void postServer(GameCtx ctx) {
		if (resultDG) {
			SCSMarker dg = new SCSMarker("dg" + target, "admin/misc_us-dg.png", BastogneSide.US);
			ctx.board.place(target, dg);
			ctx.display.createCounter(dg, ctx.board);
			ctx.display.alignStack(target);
		}
		VisualCoords pos = ctx.display.getCenter(target);
		ctx.display.clearOds(arty.toString());
		if(killSteps > 0) {
			ctx.display.showResults(pos, "");
		}
	}

	@Override
	public String toString() {
		String dg = resultDG ? ("DG-" + killSteps) : "Ineffective";
		String killDice = killRoll > 0 ? ", " + killRoll : "";
		return "Barrage on " + target + " by " + arty + ": " + dg + " (" + diceRoll + killDice + ")";
	}

}
