package earl.client.games;

public class Area implements Position {
	private static final long serialVersionUID = 1L;
	private String id;

	protected Area() {
	}

	public Area(String id) {
		this.id = id;
	}

	@Override
	public String getSVGId() {
		return id;
	}

	@Override
	public String toString() {
		return getSVGId();
	}

	@Override
	public int hashCode() {
		return getSVGId().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return equals(this, obj);
	}

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
		if (a.getSVGId()== null) {
			if (other.getSVGId() != null) {
				return false;
			}
		} else if (!a.getSVGId().equals(other.getSVGId())) {
			return false;
		}
		return true;
	}

}
