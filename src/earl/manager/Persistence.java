package earl.manager;

import earl.client.data.Board;
import earl.client.games.Bastogne;

public interface Persistence {
	String persist(Bastogne game);

	Board getTable(String tableId);
}
