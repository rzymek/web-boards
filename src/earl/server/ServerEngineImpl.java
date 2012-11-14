package earl.server;

import java.util.Random;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import earl.client.data.Board;
import earl.client.games.Game;
import earl.client.remote.ServerEngine;
import earl.manager.GameManager;

public class ServerEngineImpl extends RemoteServiceServlet implements ServerEngine {
	private static final long serialVersionUID = 1L;
	private Random random = new Random();

	@Override
	public Board getState(String tableId) {
		GameManager manager = GameManager.get();
		Game game = manager.getGame(tableId);
		return game.getBoard();
	}

	@Override
	public int roll(int d, int sides) {
		int sum = 0;
		for (int i = 0; i < d; ++i) {
			sum += random.nextInt(sides) + 1;
		}
//		notifyListeners(tableId, d + "d" + sides + " = " + sum);
		return sum;
	}
}
