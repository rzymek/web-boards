package earl.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import earl.client.data.Board;
import earl.client.data.Counter;
import earl.client.games.Game;
import earl.server.Op;
import earl.server.ex.EarlServerException;
import earl.server.persistence.PersistenceFactory;


public class GameManager {
	private static GameManager instance;
	private final Map<String,Game> games = new HashMap<String, Game>();

	public static GameManager get() {
		if(instance == null) {
			instance = new GameManager();
		}
		return instance;
	}

	public String start(Game game) {
		String id = UUID.randomUUID().toString();		
		games.put(id, game);
		PersistenceFactory.get().persist(game);
		return id;
	}

	public Collection<Game> getInvitationsFor(String user) {
		boolean participating = false;
		return filterGamesByParticipation(user, participating);
	}

	protected Collection<Game> filterGamesByParticipation(String user, boolean participating) {
		Set<Entry<String, Game>> entrySet = games.entrySet();
		Collection<Game> results = new HashSet<Game>();
		for (Entry<String, Game> entry : entrySet) {
			Game game = entry.getValue();
			if(participating == game.isParticipating(user)) {
				results.add(game);
			}
		}
		return results;
	}

	public Collection<Table> getParticipatingIn(String user) {
		boolean participating = true;
		Collection<Table> tables = new ArrayList<Table>();
		
		for (Entry<String, Game> game : games.entrySet()) {
			Table t = new Table();
			t.id = game.getKey();
			t.opponent = Arrays.asList(game.getValue().getPlayers()).toString();
		}
		return tables;
	}

	public Game getGame(String tableId) {
		Game game = games.get(tableId);
		if(game == null) {
			game = PersistenceFactory.get().getTable(tableId);
			if(game == null) {
				throw new EarlServerException("No such game: "+tableId);
			}
			games.put(tableId, game);
		}
		return game;
	}

	public void join(String tableId, String username) {
		// TODO Auto-generated method stub
		
	}

	public void deleteListener(String clientId) {
		// TODO Auto-generated method stub
		
	}

	public void counterMoved(String tableId, String counterId, String fromId, String toId) {
		Game game = getGame(tableId);
		Board board = game.getBoard();
		Counter c = board.getCounter(counterId);
		c.setPosition(board.getHex(toId));
		PersistenceFactory.get().saveCounterPosition(tableId, counterId, toId);
	}

	public void counterChanged(Counter piece) {
		// TODO Auto-generated method stub
		
	}

	public void log(String tableId, Op op) {
		PersistenceFactory.get().save(tableId, op);
	}

}
