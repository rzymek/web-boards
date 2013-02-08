package webboards.client.games.scs;

import java.io.Serializable;

import webboards.client.data.CounterInfo;
import webboards.client.data.ref.CounterId;
import webboards.client.games.scs.bastogne.BastogneSide;

public class SCSMarker extends CounterInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private String desc;
	private String path;

	protected SCSMarker() {
		super(null);
	}
	
	public SCSMarker(String id, String path, BastogneSide side) {
		super(new CounterId(id));
		this.path = path;
	}

	@Override
	public String getState() {
		return path;
	}

	public void setDescription(String desc) {
		this.desc = desc;
	}
	@Override
	public String toString() {
		return desc;
	}

}
