package earl.server.persistence;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;

import earl.client.data.Board;
import earl.client.data.Counter;
import earl.client.data.Hex;
import earl.client.games.Bastogne;
import earl.client.games.Game;
import earl.manager.Persistence;
import earl.server.ex.EarlServerException;
import earl.server.notify.TableListener;

public class DataStorePersistence implements Persistence {

	private final DatastoreService service;

	public DataStorePersistence(DatastoreService service) {
		this.service = service;
	}

	@Override
	public String persist(Game game) {
		String name = UUID.randomUUID().toString();
		Key key = KeyFactory.createKey("table", name);
		Entity table = new Entity(key);
		table.setProperty("created", new Date());
		String[] players = game.getPlayers();
		for (int i = 0; i < players.length; i++) {
			table.setProperty("player" + i, players[i]);
		}
		service.put(table);
		return name;
	}

	@Override
	public Game getTable(String tableId) {
		try {
			Map<String, Entity> counterState = getCountersState(tableId);
			Bastogne game = new Bastogne();
			game.setupScenarion52();
			Board board = game.getBoard();
			Collection<Counter> counters = board.getCounters();
			for (Counter counter : counters) {
				Entity entity = counterState.get(counter.getId());
				if(entity == null) {
					continue;
				}
				String hexId = (String) entity.getProperty("position");
				Hex hex = board.getHex(hexId);
				counter.setPosition(hex);
			}
			return game;
		} catch (Exception e) {
			throw new EarlServerException(e);
		}
	}

	private Map<String, Entity> getCountersState(String tableId) {
		Query query = new Query(tableId);
		Iterable<Entity> results = service.prepare(query).asIterable();
		Map<String,Entity> counterState = new HashMap<String, Entity>();
		for (Entity entity : results) {
			String counterId = entity.getKey().getName();
			counterState.put(counterId, entity);
		}
		return counterState;
	}

	@Override
	public List<TableListener> getListeners(String tableId) {
		return Collections.emptyList();
	}

	@Override
	public void delete(TableListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveCounterPosition(String tableId, String counterId, String hexId) {
		try {
			Key key = KeyFactory.createKey(tableId, counterId);
			Entity table = new Entity(key);
			table.setProperty("position", hexId);
			service.put(table);
		} catch (Exception e) {
			throw new EarlServerException(e);
		}
	}

}
