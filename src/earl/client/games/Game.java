package earl.client.games;

import earl.client.data.Board;

public interface Game {
	boolean isParticipating(String user);

	Board getBoard();
}
