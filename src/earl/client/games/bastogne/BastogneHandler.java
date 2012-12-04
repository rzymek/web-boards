package earl.client.games.bastogne;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gwt.core.shared.GWT;

import earl.client.data.Counter;
import earl.client.data.Hex;
import earl.client.display.Display;
import earl.client.display.DisplayChangeListener;
import earl.client.display.DisplayHandler;
import earl.client.games.Bastogne;
import earl.client.games.SCSCounter;
import earl.client.remote.ServerEngine;
import earl.client.remote.ServerEngineAsync;
import earl.client.utils.AbstractCallback;

public class BastogneHandler extends DisplayHandler {

	private final Bastogne game;
	private final Map<Hex, Hex> attacks = new HashMap<Hex, Hex>();
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
					joinAttack(defending, attacking);
					setSelectedPiece(null);
					display.showAttacks(attacks, game);
					sendAttacks();
					return;
				}
			}
		}
		super.pieceClicked(piece);
	}

	private void sendAttacks() {
		ServerEngineAsync service = GWT.create(ServerEngine.class);
		
		Map<String,String> attackHexes = new HashMap<String, String>(attacks.size());
		for (Entry<Hex, Hex> e : attacks.entrySet()) {
			attackHexes.put(e.getKey().getId(), e.getValue().getId());
		}
		service.setAttacks(attackHexes, new AbstractCallback<Void>(){});
	}

	protected Set<Hex> joinAttack(SCSCounter defending, SCSCounter attacking) {
		attacks.put(attacking.getPosition(), defending.getPosition());
		Set<Hex> attackers = new HashSet<Hex>();
		for (Entry<Hex, Hex> e : attacks.entrySet()) {
			Hex def = e.getValue();
			Hex att = e.getKey();
			if (def.equals(defending)) {
				attackers.add(att);
			}
		}
		return attackers;
	}
}
