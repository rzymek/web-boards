package earl.client.games.scs;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import earl.client.data.Counter;
import earl.client.data.Hex;
import earl.client.data.ref.HexRef;
import earl.client.display.BasicDisplayHandler;
import earl.client.display.Position;
import earl.client.games.scs.bastogne.Bastogne;
import earl.client.games.scs.bastogne.BastogneSide;
import earl.client.games.scs.ops.PerformAttack;
import earl.client.ops.Operation;

public class SCSDisplayHandler extends BasicDisplayHandler {

	private final Map<Hex, Hex> attacks = new HashMap<Hex, Hex>();
	private final Bastogne game;
	private final BastogneSide player;

	public SCSDisplayHandler(Bastogne game, BastogneSide side) {
		this.game = game;
		this.player = side;
	}

	@Override
	protected Operation moveToHex(Hex area) {
		if(area.getStack().isEmpty()) {
			return super.moveToHex(area);
		}else{
			Counter piece = area.getStack().get(0);
			if(piece instanceof SCSCounter && selectedPiece instanceof SCSCounter) {
				SCSCounter target = (SCSCounter) piece;
				SCSCounter selected = (SCSCounter) selectedPiece;
				if (target.getOwner() == player) {
					return super.moveToHex(area);
				} else {					
					if(selected.isRanged()) {
						Hex from = selected.getPosition();
						Position start = display.getCenter(from);
						Position end = display.getCenter(target.getPosition());
						display.drawArrow(start, end, from);
						setSelectedPiece(null);
						return null;
					}else if (isAdjacent(target, selectedPiece)) {
						Hex targetHex = target.getPosition();
						Hex attacking = selectedPiece.getPosition();
						attacks.put(attacking, targetHex);
						Position start = display.getCenter(attacking);
						Position end = display.getCenter(targetHex);
						display.drawArrow(start, end, attacking);
						int[] odds = game.calculateOdds(targetHex, getAttacking(targetHex));
						display.drawOds(display.getCenter(targetHex), odds);
						setSelectedPiece(null);
						return null;
					}
				}
			}else{
				return super.moveToHex(area);
			}
		}
		return null;
	}
	
	
	private Set<Hex> getAttacking(Hex area) {
		Set<Hex> attacking = new HashSet<Hex>();
		for (Entry<Hex, Hex> e : attacks.entrySet()) {
			Hex from = e.getKey();
			Hex target = e.getValue();
			if (area.equals(target)) {
				attacking.add(from);
			}
		}
		return attacking;
	}
	@Override
	protected Operation startStackSelection(Hex area) {
		Set<Hex> attacking = getAttacking(area);
		if(!attacking.isEmpty()) {
			SCSCounter target = getAnyCounterAt(area);
			if(target == null) {
				//I think this can't happen, but let's be safe
				return super.startStackSelection(area);
			}
			PerformAttack op = new PerformAttack();
			op.targetRef=area.ref();
			op.attackingRef = new HexRef[attacking.size()];
			int idx = 0;
			for (Hex hex : attacking) {
				op.attackingRef[idx++] = hex.ref();
			}
			return op;
		} else {
			display.clearResults(display.getCenter(area));
			return super.startStackSelection(area);
		}
	}

	private SCSCounter getAnyCounterAt(Hex area) {
		List<Counter> stack = area.getStack();
		for (Counter counter : stack) {
			if(counter instanceof SCSCounter){
				return (SCSCounter) counter;
			}
		}
		return null;
	}

	private boolean isAdjacent(Counter a, Counter b) {
		List<Hex> adjacent = game.getBoard().getAdjacent(a.getPosition());
		return adjacent.contains(b.getPosition());
	}

	@Override
	public void setSelectedPiece(Counter piece) {
		display.clearMarks();
		if(piece instanceof SCSCounter) {
			SCSCounter counter = (SCSCounter) piece;
			if(counter != null && counter.getOwner() != player) {
				return; //not your counter
			}else{
//				showMoves(counter);
			}
		}
		selectedPiece = piece;
		display.select(selectedPiece);
	}

	public void showMoves(SCSCounter counter) {
		int movementLeft = counter.getMovement();
		Map<Hex, Integer> costInfo = new HashMap<Hex, Integer>();		
		calulateMovesFrom(counter.getPosition(), movementLeft, costInfo);
		display.mark(costInfo.keySet());
	}

	private void calulateMovesFrom(Hex start, int movementLeft, Map<Hex, Integer> costInfo) {
		List<Hex> adj = game.getBoard().getAdjacent(start);
		for (Hex hex : adj) {
			calculateMoveTo(hex, movementLeft, costInfo);
		}
	}

	private void calculateMoveTo(Hex hex, int movementLeft, Map<Hex, Integer> costInfo) {
		int cost = game.getMovementCost(hex);
		int afterMoveMPs = movementLeft - cost;
		if(afterMoveMPs < 0) {
			return;
		}
		Integer otherPathCost = costInfo.get(hex);
		boolean notVisitedYet = (otherPathCost == null);
		if(notVisitedYet) {
			costInfo.put(hex, afterMoveMPs);
			calulateMovesFrom(hex, afterMoveMPs, costInfo);
		}else{
			boolean thisIsCheaperPath = (afterMoveMPs > otherPathCost);
			if(thisIsCheaperPath) {
				costInfo.put(hex, afterMoveMPs);
				calulateMovesFrom(hex, afterMoveMPs, costInfo);				
			}
		}
	}

	@Override
	public Counter getSelectedPiece() {
		return selectedPiece;
	}
}
