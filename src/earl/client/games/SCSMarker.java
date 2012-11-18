package earl.client.games;

import java.io.Serializable;

import earl.client.data.Counter;

public class SCSMarker extends Counter implements Serializable {

	private String desc;
	private String id;
	private String path;
//	private BastogneSide side;

	protected SCSMarker() {
	}
	
	public SCSMarker(String id, String path, BastogneSide side) {
		this.id = id;
		this.path = path;
//		this.side = side;
	}

	@Override
	public String getState() {
		return path;
	}

	@Override
	public String getId() {
		return id;
	}

	public void setDescription(String desc) {
		this.desc = desc;
	}
	@Override
	public String toString() {
		return desc;
	}

}
