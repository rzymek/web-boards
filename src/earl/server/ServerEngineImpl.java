package earl.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import earl.client.data.Board;
import earl.client.games.Game;
import earl.client.remote.ServerEngine;
import earl.manager.GameManager;

public class ServerEngineImpl  extends RemoteServiceServlet implements ServerEngine{

	@Override
	public Board getState(String tableId) {
		GameManager manager = GameManager.get();
		Game game = manager.getGame(tableId);
		return game.getBoard();
	}

}
