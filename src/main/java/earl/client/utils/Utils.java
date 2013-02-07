package earl.client.utils;

import earl.client.games.Position;

public class Utils {

	public static boolean equals(Position a, Object obj) {
		if (a == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Position)) {
			return false;
		}
		Position other = (Position) obj;
		return Utils.equals(other.getSVGId(), a.getSVGId());
	}

	public static boolean equals(Object a, Object b) {
		if (b == null) {
			if (a != null) {
				return false;
			}
		} else if (!b.equals(a)) {
			return false;
		}
		return true;
	}

}
