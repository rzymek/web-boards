package earl.client.data.ref;

import java.io.Serializable;

import earl.client.data.Identifiable;

public class Counter extends Identifiable implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;

	public Counter() {
	}

	public Counter(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}
	@Override
	public String toString() {
		return id;
	}
}
