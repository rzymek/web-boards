package webboards.client.ops.generic;

import webboards.client.ops.AbstractOperation;
import webboards.client.ops.Undoable;


public class OpponentConnected extends AbstractOperation implements Undoable {
	private static final long serialVersionUID = 1L;
	private String user;

	public OpponentConnected(String user) {
		this.user = user;
	}

	public OpponentConnected() {
	}
	@Override
	public String toString() {
		return user+" connected.";
	}

}
