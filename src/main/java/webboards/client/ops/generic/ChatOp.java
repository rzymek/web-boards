package webboards.client.ops.generic;

import webboards.client.ops.AbstractOperation;
import webboards.client.ops.Undoable;


public class ChatOp extends AbstractOperation implements Undoable {
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
