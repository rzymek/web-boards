package earl.client.games.scs;

import java.io.Serializable;

import earl.client.data.Counter;
import earl.client.games.scs.bastogne.BastogneSide;

public class SCSMarker extends Counter implements Serializable {
	private static final long serialVersionUID = 1L;
	private String desc;
	private String id;
	private String path;

	protected SCSMarker() {
	}
	
	public SCSMarker(String id, String path, BastogneSide side) {
		this.id = id;
		this.path = path;
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
