package earl.client.ops.generic;

import earl.client.ops.Operation;

public class ChatOp extends Operation {
	private static final long serialVersionUID = 1L;
	private String msg;

	protected ChatOp() {
	}
	public ChatOp(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return msg;
	}

}
