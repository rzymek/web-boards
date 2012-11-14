package earl.server;

import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import earl.client.data.Board;
import earl.client.games.Game;
import earl.client.remote.ServerEngine;
import earl.manager.GameManager;
import earl.server.notify.Notify;
import earl.server.utils.HttpUtils;

public class ServerEngineImpl extends RemoteServiceServlet implements ServerEngine {
	private static final long serialVersionUID = 1L;
	private Random random = new Random();
	private Notify notify = new Notify();

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
		String tableId = getTableId();
		if(tableId != null) {
			notify.notifyListeners(tableId, d + "d" + sides + " = " + sum);
		}
		return sum;
	}

	protected String getTableId() {
		String referer = getThreadLocalRequest().getHeader("referer");
		Map<String, List<String>> queryParams = HttpUtils.getQueryParams(referer);
		List<String> list = queryParams.get("table");
		if(list == null || list.isEmpty()) {
			return null;
		}
		String tableId = list.get(0);
		return tableId;
	}
}
