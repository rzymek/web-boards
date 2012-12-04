package earl.server.persistence;

import java.util.List;
import java.util.Map;

import earl.client.data.Board;
import earl.client.games.Game;
import earl.server.Op;
import earl.server.notify.TableListener;

public interface Persistence {
	String persist(Game game);

	List<TableListener> getListeners(String tableId);

	void delete(TableListener listener);

	void save(String tableId, Op op);

	String getLog(String tableId);

	void saveCounterPosition(String tableId, String counterId, String hexId, boolean flipped);

	Board getTable(String tableId, Board board);

	void save(String tableId, Map<String, String> attackHexes);

	Map<String, String> getAttacks(String tableId);
}
