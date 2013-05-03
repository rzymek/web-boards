package webboards.client.games.scs.ops;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import webboards.client.data.Board;
import webboards.client.data.GameCtx;
import webboards.client.display.VisualCoords;
import webboards.client.ex.WebBoardsException;
import webboards.client.games.Area;
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
	private transient List<Operation> moves = new ArrayList<Operation>();

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
//		apply(board);
	}

	private void apply(SCSBoard board) {
		console("apply "+result);
		{
			int stepsToRemove = result.A;
			List<SCSCounter> units = getUnits(board, combatAttacks);
			console("apply A on "+units);
			result.A += apply(stepsToRemove, units, board);
		}		
		{
			int stepsToRemove = result.D;
			List<SCSCounter> units = board.getInfo(target).getUnits();
			console("apply D on "+units);
			result.D += apply(stepsToRemove, units, board);
		}		
	}

	public static native void console(Object s) /*-{
		if (console) {
			console.log(s);
		}
	}-*/;
	
	private int apply(int stepsToRemove, List<SCSCounter> units, SCSBoard board) {
		int stepsAvailable = getSteps(units);
		console("trying to kill "+stepsToRemove+" from "+stepsAvailable);
		if(stepsToRemove >= stepsAvailable) {
			console("killing all "+units);
			for (SCSCounter unit : units) {				
				Move move = new Move(unit, new Area("Dead pool"));
				move.updateBoard(board);
				moves.add(move);
			}
			return -stepsToRemove;
		}else if(stepsToRemove > 0 && units.size() == 1) {
			SCSCounter unit = units.iterator().next();
			if(unit.getStepsLeft() == 1) {
				console("killing one-step unit: "+unit);
				Move move = new Move(unit, new Area("Dead pool"));
				move.updateBoard(board);
				moves.add(move);
				return -stepsToRemove;
			}else if(unit.getStepsLeft() == 2){
				console("flip "+unit);
				Flip op = new Flip(unit.ref());
				op.updateBoard(board);
				moves.add(op);
				return -1;
			}else{
				throw new WebBoardsException("TODO: place step loss");
			}
		}
		return 0;
	}

	private List<SCSCounter> getUnits(SCSBoard board, Collection<Hex> hexes) {
		console("getUnits from hexes: "+hexes);
		if(hexes == null) {
			return Collections.emptyList();
		}
		List<SCSCounter> units = new ArrayList<SCSCounter>();
		for (Hex hex : hexes) {
			units.addAll(board.getInfo(hex).getUnits());
		}
		return units;
	}
	
	private int getSteps(List<SCSCounter> units) {
		int sum = 0;
		for (SCSCounter unit : units) {
			sum += unit.getStepsLeft();
		}
		return sum;
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
		for (Operation move : moves) {
			move.draw(ctx);			
		}
	}

	@Override
	public void drawDetails(GameCtx ctx) {
		for (Operation move : moves) {
			move.drawDetails(ctx);
		}
	}
	
	@Override
	public String toString() {
		return "Acknowledged " + result + " at " + target;
	}

}
