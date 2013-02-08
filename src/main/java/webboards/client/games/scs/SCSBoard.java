package webboards.client.games.scs;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import webboards.client.data.Board;
import webboards.client.data.HexInfo;
import webboards.client.games.Hex;
import webboards.client.games.Position;

public class SCSBoard extends Board implements Serializable {
	private static final long serialVersionUID = 1L;
	private SCSHex[][] hexes;
	protected SCSHex[] workaround;
	protected Map<String, HexInfo> areas = new HashMap<String, HexInfo>();

	protected SCSBoard() {
	}

	public SCSBoard(SCSHex[][] hexes) {
		this.hexes = hexes;
	}

	public SCSHex get(int x, int y) {
		return hexes[x][y];
	}

	@Override
	public HexInfo getInfo(Position ref) {
		if (ref instanceof Hex) {
			Hex xy = (Hex) ref;
			return hexes[xy.x][xy.y];
		} else {
			HexInfo hex = areas.get(ref.getSVGId());
			if (hex == null) {
				areas.put(ref.getSVGId(), hex = new SCSHex());
			}
			return hex;
		}
	}
}
