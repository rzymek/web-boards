package earl.client.games;

import java.io.Serializable;

import earl.client.data.Counter;

public class SCSCounter extends Counter implements Serializable {

	private String description;
	private String frontPath = null;
	private String id;
	private String back;
	boolean flipped = false;

	protected SCSCounter() {
	}

	public SCSCounter(String id, String frontPath, String back) {
		this.id = id;
		this.frontPath = frontPath;
		this.back = back;
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

	@Override
	public String getState() {
		return flipped ? back : frontPath;
	}
	
	@Override
	public void flip() {
		if(back != null) {
			flipped = !flipped;
		}
	}
}
