package webboards.client.ex;

import webboards.client.ops.Operation;

public class ConcurrentOpException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public Operation op;

	@SuppressWarnings("unused")
	private ConcurrentOpException() {
	}

	public ConcurrentOpException(String msg, Operation op) {
		super(msg);
		this.op = op;
	}

}
