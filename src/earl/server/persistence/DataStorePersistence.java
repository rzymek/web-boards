package earl.server.persistence;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import earl.client.data.Board;
import earl.client.games.Bastogne;
import earl.client.games.BastogneSide;
import earl.manager.Persistence;
import earl.server.notify.TableListener;

public class DataStorePersistence implements Persistence {

	private final DatastoreService service;

	public DataStorePersistence(DatastoreService service) {
		this.service = service;
	}

	@Override
	public String persist(Bastogne game) {
		String name = UUID.randomUUID().toString();
		Key key = KeyFactory.createKey("table", name);
		Entity table = new Entity(key);
		table.setProperty("created", new Date());
		table.setProperty("playerUS", game.getPlayer(BastogneSide.US));
		table.setProperty("playerGE", game.getPlayer(BastogneSide.GE));		
		service.put(table);
		return name;
	}
	
	@Override
	public Board getTable(String tableId) {
		try {
			Key key = KeyFactory.createKey("table", tableId);
			Entity table = service.get(key);
			return new Board();
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<TableListener> getListeners(String tableId) {
		return Collections.emptyList();
	}

	@Override
	public void delete(TableListener listener) {
		// TODO Auto-generated method stub
		
	}

}
