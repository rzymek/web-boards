package earl.client.bastogne.op;

import earl.client.data.Board;
import earl.client.op.Operation;

public class OpponentConnected extends Operation{

	private String user;

	public OpponentConnected(String user) {
		this.user = user;
	}

	public OpponentConnected() {
	}

	@Override
	public String encode() {
		return user;
	}

	@Override
	public void decode(Board board, String s) {
		this.user = s;
	}

}
