package earl.client.display.handler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import earl.client.bastogne.op.PerformAttack;
import earl.client.data.Counter;
import earl.client.data.Hex;
import earl.client.games.Bastogne;
import earl.client.games.BastogneSide;
import earl.client.games.SCSCounter;
import earl.client.op.Operation;
import earl.client.op.Position;

public class SCSDisplayHandler extends BasicDisplayHandler {

	private final Map<Hex, Hex> attacks = new HashMap<Hex, Hex>();
	private final Bastogne game;

	public SCSDisplayHandler(Bastogne game) {
		this.game = game;
	}

	@Override
	protected Operation moveToHex(Hex area) {
		if(area.getStack().isEmpty()) {
			return super.moveToHex(area);
		}else{
			SCSCounter counter = (SCSCounter) area.getStack().get(0);
			BastogneSide player = BastogneSide.US;//TODO
			if (counter.getOwner() == player) {
				return super.moveToHex(area);
			} else {
				if (isAdjacent(counter, selectedPiece)) {
					Hex target = counter.getPosition();
					Hex attacking = selectedPiece.getPosition();
					attacks.put(attacking, target);
					Position start = display.getCenter(attacking);
					Position end = display.getCenter(target);
					display.drawArrow(start, end, attacking.getId());
					int[] odds = game.calculateOdds(target, getAttacking(target));
					display.drawOds(display.getCenter(target), odds);
					setSelectedPiece(null);
					return null;
				}
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
			PerformAttack op = new PerformAttack();
			op.target=area;
			op.attacking = attacking.toArray(new Hex[attacking.size()]);
			return op;
		} else {
			return super.startStackSelection(area);
		}
	}

	private boolean isAdjacent(Counter a, Counter b) {
		return true;
	}

	@Override
	public void setSelectedPiece(Counter piece) {
		selectedPiece = piece;
		display.select(selectedPiece);
	}

	@Override
	public Counter getSelectedPiece() {
		return selectedPiece;
	}
}
