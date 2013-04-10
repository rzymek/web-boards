package webboards.client.ops;

import webboards.client.data.GameCtx;
import webboards.client.ex.WebBoardsException;

public class NotImplemented extends Operation {
	private static final long serialVersionUID = 1L;
	private String msg;

	@SuppressWarnings("unused")
	private NotImplemented() {
	}

	public NotImplemented(String msg) {
		this.msg = msg;
	}

	@Override
	public void draw(GameCtx ctx) {
		throw new WebBoardsException("not implemented");
	}

	@Override
	public String toString() {
		return "Not implemented: " + msg;
	}

}
