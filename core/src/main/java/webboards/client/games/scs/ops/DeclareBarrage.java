package webboards.client.games.scs.ops;

import java.util.Collection;

import webboards.client.data.GameCtx;
import webboards.client.games.Hex;
import webboards.client.games.scs.SCSBoard;
import webboards.client.games.scs.SCSColor;
import webboards.client.games.scs.SCSCounter;
import webboards.client.games.scs.SCSHex;
import webboards.client.ops.Operation;

public class DeclareBarrage extends Operation {
	private static final long serialVersionUID = 1L;
	private SCSCounter arty;
	private Hex target;

	@SuppressWarnings("unused")
	private DeclareBarrage() {
	}

	public DeclareBarrage(SCSCounter arty, Hex target) {
		this.arty = arty;
		this.target = target;
	}

	@Override
	public void draw(GameCtx ctx) {
		SCSBoard board = (SCSBoard) ctx.board;
		Collection<SCSCounter> attacking = board.getBarragesOn(target);
		if (attacking.contains(arty)) {
			board.undeclareBarrageOf(arty);
			ctx.display.clearOds(arty.ref().toString());
			ctx.display.clearArrow(PerformBarrage.getArrowId(arty));
		} else {
			board.declareBarrage(arty, target);
			ctx.display.drawArrow(arty.getPosition(), target, PerformBarrage.getArrowId(arty), SCSColor.DELCARE.getColor());
			SCSHex hex = (SCSHex) ctx.board.getInfo(target);
			Integer value = (int) hex.applyBarrageModifiers(arty.getAttack());
			ctx.display.drawOds(ctx.display.getCenter(target), value.toString(), arty.ref().toString());
		}
	} 
  
	@Override
	public String toString() {
		return "Declare barrage " + arty + " -> " + target;
	}

}
