package webboards.client.games.scs.ops;

import java.util.ArrayList;
import java.util.Collection;

import webboards.client.data.Board;
import webboards.client.data.GameCtx;
import webboards.client.display.VisualCoords;
import webboards.client.games.Hex;
import webboards.client.games.scs.CombatResult;
import webboards.client.games.scs.SCSBoard;
import webboards.client.games.scs.SCSCounter;
import webboards.client.ops.Operation;

public class AcknowlegeResults extends Operation {
	private static final long serialVersionUID = 1L;
	private Hex target;
	private CombatResult result;
	private transient Collection<Hex> attacking;

	@SuppressWarnings("unused")
	private AcknowlegeResults() {}
	
	public AcknowlegeResults(Hex target, CombatResult combatResult) {
		this.target = target;
		this.result = combatResult;
	}

	@Override
	public void updateBoard(Board b) {
		SCSBoard board = (SCSBoard) b;
		attacking = new ArrayList<Hex>(board.getAttacking(target));
		Collection<SCSCounter> barrages = board.getBarragesOn(target);
		for (SCSCounter arty : barrages) {
			Hex hex = (Hex) arty.getPosition();
			attacking.add(hex);
			board.clearBarrageOf(arty);
		}
		board.clearAttacksOn(target);
		board.combatResultsShown.remove(target);
	}

	@Override
	public void draw(GameCtx ctx) {
		VisualCoords pos = ctx.display.getCenter(target);		
		ctx.display.clearResults(pos);
		for (Hex from : attacking) {
			ctx.display.clearArrow("combat_" + from.getSVGId());			
			ctx.display.clearArrow("barrage_" + from.getSVGId());			
		}
	}

	@Override
	public String toString() {
		return "Acknowledged " + result + " at " + target;
	}

}
