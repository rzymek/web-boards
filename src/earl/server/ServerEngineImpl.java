package earl.server;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import earl.client.data.Counter;
import earl.client.data.GameInfo;
import earl.client.data.Hex;
import earl.client.remote.ServerEngine;
import earl.manager.GameManager;
import earl.server.notify.Notify;
import earl.server.utils.HttpUtils;

public class ServerEngineImpl extends RemoteServiceServlet implements ServerEngine {
	private static final long serialVersionUID = 1L;
	private Random random = new Random();
	private Notify notify = new DisabledNotify();

	@Override
	public GameInfo getState(String tableId) {
		GameManager manager = GameManager.get();
		GameInfo info = new GameInfo();
		info.channelToken = notify.openChannel(tableId, getUser());
		info.game = manager.getGame(tableId);
		return info;
	}

	private String getUser() {
		Principal principal = getThreadLocalRequest().getUserPrincipal();
		if(principal == null) {
//			throw new SecurityException("Not logged in.");
			return null;
		}
		return principal.getName();
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
	
	@Override
	public void counterChanged(Counter piece) {
		GameManager.get().counterChanged(piece);
	}
	
	@Override
	public void counterMoved(Counter counter, Hex from, Hex to) {
		String tableId = getTableId();
		GameManager.get().counterMoved(tableId, counter, from, to);
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
