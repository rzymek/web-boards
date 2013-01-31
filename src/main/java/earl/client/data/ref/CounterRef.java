package earl.client.data.ref;

import java.io.Serializable;

public class CounterRef implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;

	public CounterRef() {
	}

	public CounterRef(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
}
