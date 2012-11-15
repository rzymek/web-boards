package earl.client.games;

import java.io.Serializable;

import earl.client.data.Board;

public interface Game extends Serializable {
	boolean isParticipating(String user);

	Board getBoard();

	String[] getPlayers();
}
