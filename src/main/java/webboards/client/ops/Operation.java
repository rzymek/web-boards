package webboards.client.ops;

import java.io.Serializable;

import webboards.client.data.Board;
import webboards.client.data.GameCtx;
import webboards.client.games.scs.bastogne.BastogneSide;

public abstract class Operation implements Serializable {
	private static final long serialVersionUID = 1L;
	public BastogneSide author;

	public void updateBoard(Board board) {
	}

	public void drawDetails(GameCtx ctx) {
	}

	public void serverExecute(ServerContext ctx) {
	}

	public void postServer(GameCtx ctx) {
	}

	@Override
	public abstract String toString();

	public void draw(GameCtx ctx) {
	}
}
