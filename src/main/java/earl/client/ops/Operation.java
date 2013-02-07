package earl.client.ops;

import java.io.Serializable;

import earl.client.data.Board;
import earl.client.data.GameCtx;
import earl.client.display.EarlDisplay;
import earl.client.games.scs.bastogne.BastogneSide;

public abstract class Operation implements Serializable {
	private static final long serialVersionUID = 1L;
	public BastogneSide author;
	public void updateBoard(Board board){
	}

	public void drawDetails(EarlDisplay g) {
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
