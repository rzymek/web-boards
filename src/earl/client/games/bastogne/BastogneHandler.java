package earl.client.games.bastogne;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import earl.client.ClientEngine;
import earl.client.data.Counter;
import earl.client.display.Display;
import earl.client.display.DisplayChangeListener;
import earl.client.display.DisplayHandler;
import earl.client.games.Bastogne;
import earl.client.games.SCSCounter;

public class BastogneHandler extends DisplayHandler {

	private final Bastogne game;
	private final Map<SCSCounter, SCSCounter> attacks = new HashMap<SCSCounter, SCSCounter>();
	private final Display display;

	public BastogneHandler(Bastogne game, Display display, DisplayChangeListener listener) {
		super(listener);
		this.game = game;
		this.display = display;
	}

	@Override
	public void pieceClicked(Counter piece) {
		if (piece instanceof SCSCounter) {
			SCSCounter defending = (SCSCounter) piece;
			SCSCounter attacking = (SCSCounter) super.getSelectedPiece();
			if (attacking != null) {
				if (!defending.getOwner().equals(attacking.getOwner())) {
					super.pieceClicked(piece);
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
						setSelectedPiece(null);
						display.showAttacks(attacks);
					}
					return;
				}
			}
		}
		super.pieceClicked(piece);
	}

	protected Set<SCSCounter> joinAttack(SCSCounter defending, SCSCounter attacking) {
		attacks.put(attacking, defending);
		Set<SCSCounter> attackers = new HashSet<SCSCounter>();
		for (Entry<SCSCounter, SCSCounter> e: attacks.entrySet()) {
			SCSCounter def = e.getValue();
			SCSCounter att = e.getKey();
			if(def.equals(defending)) {
				attackers.add(att);
			}
		}
		return attackers;
	}
}
