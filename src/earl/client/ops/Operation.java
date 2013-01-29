package earl.client.ops;

import java.io.Serializable;

import earl.client.data.Board;
import earl.client.display.EarlDisplay;
import earl.client.games.scs.bastogne.BastogneSide;

public abstract class Operation implements Serializable {
	private static final long serialVersionUID = 1L;
	public BastogneSide author;
	public void draw(Board board, EarlDisplay g) {		
	}

	public void clientExecute(Board board){
	}

	public void drawDetails(EarlDisplay g) {
	}

	public void serverExecute(ServerContext ctx) {
	}

	public void postServer(Board board, EarlDisplay basicDisplay) {
	}
}
