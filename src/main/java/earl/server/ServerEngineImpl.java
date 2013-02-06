package earl.server;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import net.sf.jsr107cache.CacheException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Work;
import com.googlecode.objectify.cmd.LoadType;
import com.googlecode.objectify.cmd.Query;

import earl.client.data.Counter;
import earl.client.data.Game;
import earl.client.data.GameInfo;
import earl.client.ex.EarlServerException;
import earl.client.games.Ref;
import earl.client.games.scs.bastogne.Bastogne;
import earl.client.games.scs.bastogne.BastogneSide;
import earl.client.ops.Operation;
import earl.client.ops.ServerContext;
import earl.client.remote.ServerEngine;
import earl.server.entity.GameState;
import earl.server.entity.OperationEntity;
import earl.server.entity.Table;
import earl.server.notify.Notify;
import earl.server.utils.HttpUtils;
import earl.server.utils.ServerUtils;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class ServerEngineImpl extends RemoteServiceServlet implements ServerEngine {
	private static final Logger log = Logger.getLogger(ServerEngineImpl.class.getName());

	private static final long serialVersionUID = 1L;
	private transient final Notify notify = new Notify();

	private transient static Map<String, Game> games = Collections.synchronizedMap(new HashMap<String, Game>());

	public ServerEngineImpl() throws CacheException {		
	}

	@Override
	protected void doUnexpectedFailure(Throwable e) {
		e.printStackTrace();
		super.doUnexpectedFailure(e);
	}

	@Override
	public GameInfo getState(String tableId) {
		try {
			long tid = Long.parseLong(tableId);
			Table table = ofy().load().type(Table.class).id(tid).get();
			if (table == null) {
				throw new EarlServerException("Invalid table id=" + tableId);
			}
			GameInfo info = new GameInfo();
			info.channelToken = notify.openChannel(table, getUser());
			String user = getUser();
			if (user.equals(table.player1)) {
				info.side = BastogneSide.US;
			} else if (user.equals(table.player2)) {
				info.side = BastogneSide.GE;
			} else {
				// not your game
				if (table.player1 == null) {
					info.joinAs = BastogneSide.US;
				} else if (table.player2 == null) {
					info.joinAs = BastogneSide.GE;
				} else {
					throw new EarlServerException("This is not your game");
				}
			}
	
			GameState state = ofy().load().type(GameState.class).parent(table).id(user).get();
			info.state = loadState(info, state);
			info.ops = loadOps(tid, state);
	
			Bastogne game = new Bastogne();
			game.load(info.ops, null);
			info.game = game;
			return info;
		}catch(RuntimeException ex){
			ex.printStackTrace();
			throw ex;
		}
	}

	public Map<String, Ref> loadState(GameInfo info, GameState state) {
		if(state == null) {
			return new HashMap<String, Ref>();
		}
		return state.state;
	}
	
	public GameState save(Game game, long tableId, String user, Date timestamp) {
		Collection<Counter> counters = game.getBoard().getCounters();
		Key<Table> table = Key.create(Table.class, tableId);		
		GameState state = new GameState();
		state.table = table;		
		state.user = user;
		state.updated = timestamp;
		state.state = new HashMap<String, Ref>();
		
		for (Counter counter : counters) {
			Ref hexPos = counter.getPosition();
			String counterPos = counter.ref().getId();
			state.state.put(counterPos, hexPos);
		}
		ofy().save().entity(state);
		return state;
	}

	@Override
	public GameInfo undo(long tid) {
		String user = getUser();
		List<OperationEntity> opEnts = loadOpEntities(tid, null);
		if(!opEnts.isEmpty()) {
			OperationEntity last = opEnts.get(opEnts.size()-1);
			ofy().delete().entity(last).now();
			opEnts.remove(opEnts.size()-1);
		}
		Bastogne game = new Bastogne();
		List<Operation> ops = unwrap(opEnts);
		game.load(ops, null);
		Date lastTimestamp = null;
		if(!opEnts.isEmpty()) {
			OperationEntity last = opEnts.get(opEnts.size()-1);
			lastTimestamp = last.timestamp;
		}				
		GameInfo info = new GameInfo();
		info.ops = ops;
		info.state = save(game, tid, user, lastTimestamp).state;
		return info;
	}
	
	public List<Operation> loadOps(long tableId, GameState state) {
		return unwrap(loadOpEntities(tableId, state));
	}

	public List<Operation> unwrap(Collection<OperationEntity> res) {
		List<Operation> results = new ArrayList<Operation>();
		for (OperationEntity operationEntity : res) {
			results.add(deserialize(operationEntity.data));
		}
		return results;
	}

	public List<OperationEntity> loadOpEntities(long tableId, GameState state) {
		LoadType<OperationEntity> load = ofy().load().type(OperationEntity.class);
		Query<OperationEntity> query = load.filter("sessionId", String.valueOf(tableId));
		if(state != null) {
			query = query.filter("timestamp >", state.updated);
		}
		return query.order("timestamp").list();
	}

	private Operation deserialize(byte[] data) {
		try{
			ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(data));
			try {
				return (Operation) in.readObject();
			}finally{
				in.close();
			}
		}catch (Exception e) {
			throw new EarlServerException(e);
		}
	}

	private String getUser() {
		Principal principal = getThreadLocalRequest().getUserPrincipal();
		if (principal == null) {
			throw new SecurityException("Not logged in.");
		}
		log.fine("user: "+principal.getName());
		return principal.getName();
	}

	protected String getTableId() {
		String referer = getThreadLocalRequest().getHeader("referer");
		Map<String, List<String>> queryParams = HttpUtils.getQueryParams(referer);
		List<String> list = queryParams.get("table");
		if (list == null || list.isEmpty()) {
			return null;
		}
		String tableId = list.get(0);
		return tableId;
	}

	@Override
	public Operation process(Operation op) {
		String tableId = getTableId();
		long tid = Long.parseLong(tableId);
		String user = getUser();
		op.author = getSide(tid, user);
		Bastogne bastogne = getGame(tableId);
		ServerContext ctx = new ServerContext();
		ctx.game = bastogne;
		op.updateBoard(bastogne.getBoard());
		op.serverExecute(ctx);

		OperationEntity e = new OperationEntity();
		e.sessionId = tableId;		
		e.data = ServerUtils.serialize(op);
		e.className = op.getClass().getName();
		e.timestamp = new Date();
		e.adebug = ServerUtils.describe(op);
				
		save(bastogne, tid, user, e.timestamp);
		ofy().save().entity(e);
		
		Table table = ofy().load().type(Table.class).id(tableId).get();
		notify.notifyListeners(table, op, user);
		log.info("Executed "+op);
		return op;
	}

	private BastogneSide getSide(long tableId, String user) {
		Table table = ofy().load().type(Table.class).id(tableId).get();
		if (user.equals(table.player1)) {
			return BastogneSide.US;
		} else if (user.equals(table.player2)) {
			return BastogneSide.GE;
		}else{
			return null;
		}
	}

	private Bastogne getGame(String tableId) {
		Bastogne game = (Bastogne) games.get(tableId);		
		if(game == null) {
			GameInfo info = getState(tableId);
			games.put(tableId, info.game);
		}
		return game;
	}

	@Override
	public void join(final String tableId) {
		ofy().transact(new Work<Table>() {
			@Override
			public Table run() {
				Long id = Long.valueOf(tableId);
				Table table = ofy().load().type(Table.class).id(id).get();
				if (table.player1 == null) {
					table.player1 = getUser();
				} else if (table.player2 == null) {
					table.player2 = getUser();
				} else {
					throw new EarlServerException("This game is already full");
				}
				ofy().save().entity(table).now();
				return table;
			}
		});
	}
}
