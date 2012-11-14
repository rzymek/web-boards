package earl.manager;

import java.util.List;

import earl.client.data.Board;
import earl.client.games.Bastogne;
import earl.server.notify.TableListener;

public interface Persistence {
	String persist(Bastogne game);

	Board getTable(String tableId);

	List<TableListener> getListeners(String tableId);

	void delete(TableListener listener);
}
