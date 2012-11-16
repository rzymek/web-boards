package earl.server.persistence;

import java.util.List;

import earl.client.games.Game;
import earl.server.Op;
import earl.server.notify.TableListener;

public interface Persistence {
	String persist(Game game);

	Game getTable(String tableId);

	List<TableListener> getListeners(String tableId);

	void delete(TableListener listener);

	void saveCounterPosition(String tableId, String id, String id2);

	void save(String tableId, Op op);

	String getLog(String tableId);
}
