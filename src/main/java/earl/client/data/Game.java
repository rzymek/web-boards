package earl.client.data;

import java.io.Serializable;


public interface Game extends Serializable {
	boolean isParticipating(String user);

	Board getBoard();

	String[] getPlayers();
}
