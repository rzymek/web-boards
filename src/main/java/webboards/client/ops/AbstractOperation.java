package webboards.client.ops;

import webboards.client.data.Board;
import webboards.client.data.GameCtx;

public abstract class AbstractOperation implements Operation {
	private static final long serialVersionUID = 1L;

	@Override
	public void updateBoard(Board board) {
	}

	@Override
	public void drawDetails(GameCtx ctx) {
	}

	@Override
	public void serverExecute(ServerContext ctx) {
	}

	@Override
	public void postServer(GameCtx ctx) {
	}

	@Override
	public void draw(GameCtx ctx) {
	}
	
	@Override
	public abstract String toString();

	public void undoDraw(GameCtx ctx) {
	}
	public void undoUpdate(Board board) {
	}
}
