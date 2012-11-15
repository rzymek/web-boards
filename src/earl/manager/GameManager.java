package earl.manager;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import earl.client.data.Counter;
import earl.client.data.Hex;
import earl.client.games.Game;
import earl.server.ex.EarlServerException;


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

	public Collection<Game> getParticipatingIn(String user) {
		boolean participating = true;
		return filterGamesByParticipation(user, participating);
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

	public void counterMoved(String tableId, Counter counter, Hex from, Hex to) {
		Game game = getGame(tableId);
		Counter c = game.getBoard().getCounter(counter.getId());
		c.setPosition(to);
		PersistenceFactory.get().saveCounterPosition(tableId, counter.getId(), to.getId());
	}

	public void counterChanged(Counter piece) {
		// TODO Auto-generated method stub
		
	}

}
