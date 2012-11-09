package earl.manager;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import earl.client.games.Game;


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
		return games.get(tableId);
	}

}
