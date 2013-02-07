package earl.client.games.scs;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import earl.client.data.Board;
import earl.client.data.Hex;
import earl.client.games.HexXY;
import earl.client.games.Ref;

public class SCSBoard extends Board implements Serializable {
	private static final long serialVersionUID = 1L;
	private SCSHex[][] hexes;
	protected SCSHex[] workaround;
	protected Map<String, Hex> areas = new HashMap<String, Hex>();

	protected SCSBoard() {
	}

	public SCSBoard(SCSHex[][] hexes) {
		this.hexes = hexes;
	}

	public SCSHex get(int x, int y) {
		return hexes[x][y];
	}

	@Override
	public Hex get(Ref ref) {
		if (ref instanceof HexXY) {
			HexXY xy = (HexXY) ref;
			return hexes[xy.x][xy.y];
		} else {
			Hex hex = areas.get(ref.getSVGId());
			if (hex == null) {
				areas.put(ref.getSVGId(), hex = new SCSHex());
			}
			return hex;
		}
	}
}
