package earl.client.games;

import java.io.Serializable;

import earl.client.data.Counter;

public class SCSCounter extends Counter implements Serializable {

	private String description;
	private String frontPath = null;
	private String id;

	protected SCSCounter() {
	}

	public SCSCounter(String id, String frontPath) {
		this.id = id;
		this.frontPath = frontPath;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public String getId() {
		return id;
	}

	public String getFrontPath() {
		return frontPath;
	}
}
