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
	private transient Collection<Hex> combatAttacks;
	private transient Collection<SCSCounter> barrages;

	@SuppressWarnings("unused")
	private AcknowlegeResults() {}
	
	public AcknowlegeResults(Hex target, CombatResult combatResult) {
		this.target = target;
		this.result = combatResult;
	}

	@Override
	public void updateBoard(Board b) {
		SCSBoard board = (SCSBoard) b;
		board.combatResultsShown.remove(target);
		
		Collection<Hex> combatAttacks = board.getAttacking(target);
		//create a copy
		this.combatAttacks = new ArrayList<Hex>(combatAttacks); 
		board.clearAttacksOn(target);
		
		Collection<SCSCounter> barrages = board.getBarragesOn(target);
		//create a copy
		this.barrages = new ArrayList<SCSCounter>(barrages);
		for (SCSCounter arty : barrages) {
			board.undeclareBarrageOf(arty);
		}		
	}

	@Override
	public void draw(GameCtx ctx) {
		VisualCoords pos = ctx.display.getCenter(target);		
		ctx.display.clearResults(pos);
		for (Hex from : combatAttacks) {
			ctx.display.clearArrow("combat_" + from.getSVGId());						
		}
		for (SCSCounter arty : barrages) {
			ctx.display.clearArrow("barrage_" + arty.ref());			
		}
	}

	@Override
	public String toString() {
		return "Acknowledged " + result + " at " + target;
	}

}
