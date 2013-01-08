package earl.client.bastogne.op;

import earl.client.op.Operation;

public class OpponentConnected extends Operation{
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
