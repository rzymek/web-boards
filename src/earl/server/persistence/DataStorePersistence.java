package earl.server.persistence;

import java.util.ArrayList;
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
import com.google.appengine.api.datastore.Query.SortDirection;

import earl.client.data.Board;
import earl.client.data.Counter;
import earl.client.data.Hex;
import earl.client.games.Game;
import earl.server.Op;
import earl.server.Op.Type;
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
	public Board getTable(String tableId, Board board) {
		try {
			Map<String, Entity> counterState = getCountersState(tableId);
			Collection<Counter> counters = board.getCounters();
			for (Counter counter : counters) {
				Entity entity = counterState.get(counter.getId());
				if(entity == null) {
					continue;
				}
				String hexId = (String) entity.getProperty("position");
				Hex hex = board.getHex(hexId);
				counter.setPosition(hex);
				Boolean flipped = (Boolean) entity.getProperty("flipped");
				if(flipped != null && flipped) {
					counter.flip();
				}
			}
			return board;
		} catch (Exception e) {
			throw new EarlServerException(e);
		}
	}
	@Override
	public String getLog(String tableId) {
		Query query = new Query(tableId+"-op");
		query.addSort("tstamp", SortDirection.DESCENDING);
		Iterable<Entity> results = service.prepare(query).asIterable();
		StringBuilder log = new StringBuilder();
		for (Entity entity : results) {
			String type = (String) entity.getProperty("type");
			Date tstamp = (Date) entity.getProperty("tstamp");
			List<String> args = new ArrayList<String>();
			for(int i=0; ;i++) {
				String arg = (String) entity.getProperty(String.valueOf(i));
				if(arg == null){
					break;
				}
				args.add(arg);				
			}
			Op op = new Op(Type.valueOf(type), args.toArray(new String[args.size()]));
			log.append(String.format("[%s] %s\n", tstamp, op.toString()));
		}
		return log.toString();
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
	public void saveCounterPosition(String tableId, String counterId, String hexId, boolean flipped) {
		try {
			Key key = KeyFactory.createKey(tableId, counterId);
			Entity table = new Entity(key);
			table.setProperty("position", hexId);			
			table.setProperty("flipped", flipped);
			service.put(table);
		} catch (Exception e) {
			throw new EarlServerException(e);
		}
	}

	@Override
	public void save(String tableId, Op op) {
		try {
			Entity entity = new Entity(tableId+"-op");
			entity.setProperty("type", op.getType().name());
			entity.setProperty("tstamp", new Date());
			String[] args = op.getArgs();
			for (int i = 0; i < args.length; i++) {
				entity.setProperty(String.valueOf(i), args[i]);
			}
			service.put(entity);
		} catch (Exception e) {
			throw new EarlServerException(e);
		}		
	}
}
