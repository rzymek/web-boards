package earl.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheException;
import net.sf.jsr107cache.CacheManager;

import com.google.appengine.api.datastore.Entity;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Work;
import com.googlecode.objectify.cmd.LoadType;

import earl.client.data.Counter;
import earl.client.data.Game;
import earl.client.data.GameInfo;
import earl.client.ex.EarlServerException;
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

import static com.googlecode.objectify.ObjectifyService.ofy;

public class ServerEngineImpl extends RemoteServiceServlet implements ServerEngine {
	private static final Logger log = Logger.getLogger(ServerEngineImpl.class.getName());

	private static final long serialVersionUID = 1L;
	private transient final Notify notify = new Notify();

	private transient final Cache cache;

	private transient static Map<String, Game> games = Collections.synchronizedMap(new HashMap<String, Game>());

	public ServerEngineImpl() throws CacheException {
		Map<Object, Object> config = new HashMap<Object, Object>();
//		config.put(GCacheFactory.EXPIRATION_DELTA, 3 * 60/*sec*/);
		cache = CacheManager.getInstance().getCacheFactory().createCache(config);
	}

	@Override
	public GameInfo getState(String tableId) {
		GameInfo info = new GameInfo();
		long tid = Long.parseLong(tableId);
		info.channelToken = notify.openChannel(tid, getUser());
		info.mapInfo = loadMapInfo();
		info.ops = loadOps(tableId);
		Table table = ofy().load().type(Table.class).id(tid).get();
		if (table == null) {
			throw new EarlServerException("Invalid table id=" + tableId);
		}
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
		return info;
	}
	
	public void save(Game game, long tableId, String user, Date timestamp) {
		Collection<Counter> counters = game.getBoard().getCounters();
		Key<Table> table = Key.create(Table.class, tableId);		
		GameState state = new GameState();
		state.table = table;		
		state.user = user;
		state.updated = timestamp;
		
		Entity entity = ofy().toEntity(state);
		for (Counter counter : counters) {
			String hexPos = counter.getPosition().ref().getId();
			String counterPos = counter.ref().getId();
			entity.setProperty(counterPos, hexPos);
		}
		ofy().save().entity(state);
	}

	public Collection<Operation> loadOps(String tableId) {
		LoadType<OperationEntity> load = ofy().load().type(OperationEntity.class);
		Collection<OperationEntity> res = load.filter("sessionId", tableId).order("timestamp").list();		
		res = updateWithPending(tableId, res);
		List<Operation> results = new ArrayList<Operation>();
		for (OperationEntity operationEntity : res) {
			results.add(deserialize(operationEntity.data));
		}
		return results;
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

	public Set<OperationEntity> updateWithPending(String tableId, Collection<OperationEntity> res) {
		Set<OperationEntity> results = new TreeSet<OperationEntity>(res);
		String key = (String) cache.get(tableId);
		if(key != null) {
			for(;;){
				CacheEntry c = (CacheEntry) cache.get(key);
				if(c == null) {
					break;
				}
				results.add(c.value);
				key = c.nextKey;
			}
		}
		return results;
	}

	private Map<String, String> loadMapInfo() {
		try {
			Properties p = new Properties();
			InputStream in = Bastogne.class.getResourceAsStream("/bastogne-map.properties");
			try {
				p.load(in);
			} finally {
				in.close();
			}
			Set<Entry<Object, Object>> entrySet = p.entrySet();
			Map<String, String> info = new HashMap<String, String>();
			for (Entry<Object, Object> entry : entrySet) {
				info.put((String) entry.getKey(), (String) entry.getValue());
			}
			return info;
		} catch (IOException e) {
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
		op.serverExecute(ctx);

		OperationEntity e = new OperationEntity();
		e.sessionId = tableId;		
		e.data = serialize(op);
		e.className = op.getClass().getName();
		e.timestamp = new Date();
		
		save(bastogne, tid, user, e.timestamp);
		ofy().save().entity(e);
		
		String lastKey = (String) cache.get(e.sessionId);
		CacheEntry c = new CacheEntry();
		c.nextKey = lastKey;
		c.value = e;
		String newKey = e.sessionId+e.timestamp;
		cache.put(newKey, c);
		cache.put(e.sessionId, newKey);
		notify.notifyListeners(tid, op, user);
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
			game = new Bastogne();
			GameInfo info = getState(tableId);
			game.load(info.mapInfo, info.ops, null);
			games.put(tableId, game);
		}
		return game;
	}

	private byte[] serialize(Serializable op) {
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		try {
			ObjectOutputStream out = new ObjectOutputStream(buf);
			try {
				out.writeObject(op);
			}finally{
				out.close();
			}
		}catch (IOException e) {
			throw new EarlServerException(e);
		}
		return buf.toByteArray();
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
