package webboards.client.games.scs.ops;

import java.util.Collection;

import webboards.client.data.GameCtx;
import webboards.client.games.Hex;
import webboards.client.games.scs.SCSBoard;
import webboards.client.games.scs.SCSColor;
import webboards.client.games.scs.SCSHex;
import webboards.client.ops.AbstractOperation;

public class DeclareAttack extends AbstractOperation {
	private static final long serialVersionUID = 1L;
	private Hex from;
	private Hex target;

	@SuppressWarnings("unused")
	private DeclareAttack() {
	}

	public DeclareAttack(Hex from, Hex target) {
		this.from = from;
		this.target = target;
	}

	@Override
	public void draw(GameCtx ctx) {
		SCSBoard board = (SCSBoard) ctx.board;
		Collection<Hex> attacking = board.getAttacking(target);
		if (attacking.contains(from)) {
			board.undeclareAttack(from);
			ctx.display.clearArrow("combat_" + from.getSVGId());
			updateOdsDisplay(ctx, target, board);
		} else {
			Hex prevTarget = board.getAttacks(from);
			board.declareAttack(from, target);
			if(prevTarget != null) {
				updateOdsDisplay(ctx, prevTarget, board);
			}
			ctx.display.drawArrow(from, target, "combat_" + from.getSVGId(), SCSColor.DELCARE.getColor());
			updateOdsDisplay(ctx, target, board);
		}
	}

	private void updateOdsDisplay(GameCtx ctx, Hex target, SCSBoard board) {
		SCSHex targetHex = (SCSHex) ctx.board.getInfo(target);
		Collection<SCSHex> attacking = board.getAttackingInfo(target);
		if(attacking.isEmpty()) {
			ctx.display.clearOds(target.getSVGId());
		}else{
			int[] odds = SCSBoard.calculateOdds(targetHex, attacking, target);
			String text = odds[0] + ":" + odds[1];
			ctx.display.drawOds(ctx.display.getCenter(target), text, target.getSVGId());
		}
	}

	@Override
	public String toString() {
		return "Declare attack " + from + " -> " + target;
	}

}
