package earl.server;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.LoadType;

import earl.client.data.Counter;
import earl.client.data.GameInfo;
import earl.client.data.Hex;
import earl.client.games.Bastogne;
import earl.client.op.Operation;
import earl.client.remote.ServerEngine;
import earl.manager.GameManager;
import earl.server.Op.Type;
import earl.server.ex.EarlServerException;
import earl.server.notify.Notify;
import earl.server.persistence.PersistenceFactory;
import earl.server.utils.HttpUtils;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class ServerEngineImpl extends RemoteServiceServlet implements ServerEngine {
	private static final long serialVersionUID = 1L;
	private final Random random = new Random();
	private final Notify notify = new DisabledNotify();

	@Override
	public GameInfo getState(String tableId) {
		ObjectifyService.register(OperationEntity.class);
		GameManager manager = GameManager.get();
		GameInfo info = new GameInfo();
		info.channelToken = notify.openChannel(tableId, getUser());
		info.game = manager.getGame(tableId);
		info.log = PersistenceFactory.get().getLog(tableId);
		
		LoadType<OperationEntity> load = ObjectifyService.ofy().load().type(OperationEntity.class);
		List<OperationEntity> results = load.filter("sessionId", tableId).order("id").list();
		info.ops = new ArrayList<Operation>(results.size());
		try {
			for (OperationEntity e : results) {
				Operation op = (Operation) Class.forName(e.type).newInstance();
				op.decode(info.game.getBoard(), e.data);
				info.ops.add(op);
			}
		}catch (Exception e) {
			throw new EarlServerException(e);
		}
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
		String tableId = getTableId();
		GameManager.get().counterChanged(tableId, piece.getId());
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
	
	@Override
	public void setAttacks(Map<String, String> attackHexes) {
		String tableId = getTableId();
		Bastogne bastogne = (Bastogne) GameManager.get().getGame(tableId);
		bastogne.attacks = attackHexes;
		PersistenceFactory.get().save(tableId, attackHexes);
	}

	@Override
	public void process(Operation op) {
		ObjectifyService.register(OperationEntity.class);
		OperationEntity e = new OperationEntity();
		e.sessionId = getTableId();
		e.data = op.encode();
		e.type = op.getClass().getName();
		ofy().save().entity(e);
	}
}
