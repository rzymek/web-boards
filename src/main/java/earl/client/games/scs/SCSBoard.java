package earl.client.games.scs;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import earl.client.data.Board;
import earl.client.data.HexInfo;
import earl.client.games.Hex;
import earl.client.games.Position;

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
