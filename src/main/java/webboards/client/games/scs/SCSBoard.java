package webboards.client.games.scs;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import webboards.client.data.Board;
import webboards.client.games.Hex;
import webboards.client.games.Position;

public class SCSBoard extends Board implements Serializable {
	private static final long serialVersionUID = 1L;
	private SCSHex[][] hexes;
	protected SCSHex[] workaround;
	protected Map<String, SCSHex> areas = new HashMap<String, SCSHex>();
	protected Map<Hex, Hex> attacks = new HashMap<Hex, Hex>();	

	protected SCSBoard() {
	}

	public SCSBoard(SCSHex[][] hexes) {
		this.hexes = hexes;
	}

	public SCSHex get(int x, int y) {
		return hexes[x][y];
	}

	@Override
	public SCSHex getInfo(Position ref) {
		if (ref instanceof Hex) {
			Hex xy = (Hex) ref;
			return hexes[xy.x][xy.y];
		} else {
			SCSHex hex = areas.get(ref.getSVGId());
			if (hex == null) {
				areas.put(ref.getSVGId(), hex = new SCSHex());
			}
			return hex;
		}
	}

	public void declareAttack(Hex attacking, Hex defending) {
		attacks.put(attacking, defending);
	}

	public Collection<SCSHex> getAttacking(Hex target) {
		Collection<SCSHex> result = new HashSet<SCSHex>();
		for (Entry<Hex, Hex> attack : attacks.entrySet()) {
			Hex attacking = attack.getKey();
			Hex defending = attack.getValue();
			if(defending.equals(target)) {
				SCSHex hex = getInfo(attacking);
				result.add(hex);
			}
		}
		return result;
	}
}
