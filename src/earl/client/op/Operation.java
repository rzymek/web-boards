package earl.client.op;

import java.io.Serializable;

import earl.client.data.Board;
import earl.client.data.Identifiable;
import earl.client.games.Game;

public abstract class Operation implements Serializable {
	public transient Game game;
	
	public void draw(Board board, EarlDisplay g) {		
	}

	public void clientExecute(Board board){
	}

	public void drawDetails(EarlDisplay g) {
	}

	public void serverExecute() {
	}

	public void postServer(Board board, EarlDisplay basicDisplay) {
	}

}
