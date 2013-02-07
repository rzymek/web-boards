package earl.client.utils;

import java.util.List;

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

	public static String toString(List<String> list, String sep) {
		StringBuilder buf = new StringBuilder();
		for (String string : list) {
			if(buf.length() > 0) {
				buf.append(sep);
			}
			buf.append(string);
		}
		return buf.toString();
	}

	public static boolean isEmpty(String s) {
		return s == null ? true : s.isEmpty();
	}

}
