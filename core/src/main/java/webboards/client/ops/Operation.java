package webboards.client.ops;

import java.io.Serializable;

import webboards.client.data.Board;
import webboards.client.data.GameCtx;

public abstract class Operation implements Serializable {
	private static final long serialVersionUID = 2L;
	public int index;

	public void updateBoard(Board board) {
	}

	public void drawDetails(GameCtx ctx) {
	}

	public void serverExecute(ServerContext ctx) {
	}

	public void postServer(GameCtx ctx) {
	}

	public void draw(GameCtx ctx) {
	}
	
	@Override
	public abstract String toString();

	public void undoDraw(GameCtx ctx) {
	}
	public void undoUpdate(Board board) {
	}
}
