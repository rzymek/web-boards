package earl.client.games;

import earl.client.utils.Utils;

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
		return Utils.equals(this, obj);
	}

}
