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
import earl.server.Op.Type;
import earl.server.notify.Notify;
import earl.server.persistence.PersistenceFactory;
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
		info.log = PersistenceFactory.get().getLog(tableId);
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
		int[] dice = rollDice(d, sides);
		int sum = getSum(dice);
		int[] oparg = new int[3+dice.length];
		oparg[0] = d;
		oparg[1] = sides;
		oparg[2] = sum;
		for (int i = 0; i < dice.length; i++) {
			oparg[i+3] = dice[i];
		}
		String tableId = getTableId();
		Op roll = new Op(Type.ROLL, oparg);
		GameManager.get().log(tableId, roll);
		if(tableId != null) {
			notify.notifyListeners(tableId, roll);
		}
		return sum;
	}

	private int getSum(int[] dice) {
		int sum=0;
		for (int d : dice) {
			sum += d;
		}
		return sum;
	}

	private int[] rollDice(int d, int sides) {
		int[] dice = new int[d];
		for (int i = 0; i < d; ++i) {
			int die = random.nextInt(sides) + 1;
			dice[i] = die;
		}
		return dice;
	}
	
	@Override
	public void counterFlipped(Counter piece) {
		GameManager.get().counterChanged(piece);
	}
	
	@Override
	public void counterMoved(Counter counter, Hex from, Hex to) {
		String tableId = getTableId();
		GameManager.get().counterMoved(tableId, counter.getId(), from.getId(), to.getId());
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
	
	
	@Override
	public void chat(String text) {
		String tableId = getTableId();
		PersistenceFactory.get().save(tableId, new Op(Type.CHAT, text));
	}
}
