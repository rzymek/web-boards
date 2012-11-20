package earl.client.games.bastogne;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import earl.client.ClientEngine;
import earl.client.data.Counter;
import earl.client.data.Hex;
import earl.client.display.DisplayHandler;
import earl.client.display.GameHandler;
import earl.client.games.Bastogne;
import earl.client.games.SCSCounter;

public class BastogneHandler implements GameHandler {

	private final DisplayHandler delegate;
	private final Bastogne game;
	private final Map<SCSCounter, Set<SCSCounter>> attacks = new HashMap<SCSCounter, Set<SCSCounter>>();

	public BastogneHandler(DisplayHandler displayHandler, Bastogne game) {
		this.delegate = displayHandler;
		this.game = game;
	}

	@Override
	public void areaClicked(Hex area) {
		delegate.areaClicked(area);
	}

	@Override
	public void pieceClicked(Counter piece) {
		if (piece instanceof SCSCounter) {
			SCSCounter defending = (SCSCounter) piece;
			SCSCounter attacking = (SCSCounter) delegate.getSelectedPiece();
			if (attacking != null) {
				if (!defending.getOwner().equals(attacking.getOwner())) {
					Set<SCSCounter> attackers = joinAttack(defending, attacking);					
					
					ClientEngine.log(attackers + " attacks " + defending);
					String hexId = defending.getPosition().getId();
					Map<String, String> mapInfo = game.getMapInfo();
					String hexInfo = mapInfo.get(hexId);
					if (hexInfo == null) {
						ClientEngine.log("no modifiers");
					} else {
						boolean forrest = hexInfo.contains("F");
						boolean city = hexInfo.contains("C");
						float defence = defending.getDefence();
						String s = "";
						if (forrest) {
							s += "Dx2 (forrest), ";
							defence *= 2;
						}
						if (city) {
							s += "Dx2 (village), ";
							defence *= 2;
						}
						float attack = 0;
						for (SCSCounter a : attackers) {
							attack += a.getAttack();
						}
						float smaller = Math.min(attack, defence);
						int a = Math.round(attack / smaller);
						int b = Math.round(defence / smaller);
						ClientEngine.log("Odds: "+a+":"+b+" - "+attack
								+" | "+defence+" ("+defending.getDefence() + ") mods: "+s);
						
					}
				}
			}
		}
		delegate.pieceClicked(piece);
	}

	protected Set<SCSCounter> joinAttack(SCSCounter defending, SCSCounter attacking) {
		Set<SCSCounter> attackers = attacks.get(defending);
		if(attackers == null) {
			attacks.put(defending, attackers = new HashSet<SCSCounter>());
		}
		attackers.add(attacking);
		return attackers;
	}

	@Override
	public void setSelectedPiece(Counter piece) {
		delegate.setSelectedPiece(piece);
	}

}
