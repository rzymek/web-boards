package earl.server;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
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

import com.google.appengine.api.memcache.jsr107cache.GCacheFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Work;
import com.googlecode.objectify.cmd.LoadType;

import earl.client.data.GameInfo;
import earl.client.ex.EarlServerException;
import earl.client.games.Bastogne;
import earl.client.games.BastogneSide;
import earl.client.op.OpData;
import earl.client.op.Operation;
import earl.client.remote.ServerEngine;
import earl.manager.Table;
import earl.server.notify.Notify;
import earl.server.utils.HttpUtils;
import earl.server.utils.Utils;

public class ServerEngineImpl extends RemoteServiceServlet implements ServerEngine {
	private static final Logger log = Logger.getLogger(ServerEngineImpl.class.getName());

	private static final long serialVersionUID = 1L;
	private final Notify notify = new Notify();

	private final Cache cache;

	public ServerEngineImpl() throws CacheException {
		Map<Object, Object> config = new HashMap<Object, Object>();
		config.put(GCacheFactory.EXPIRATION_DELTA, 3 * 60/*sec*/);
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
		System.out.println("user=" + user);
		System.out.println("table=" + table);
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

	public List<OpData> loadOps(String tableId) {
		LoadType<OperationEntity> load = ofy().load().type(OperationEntity.class);
		List<OperationEntity> res = load.filter("sessionId", tableId).order("timestamp").list();		
		Collection<OperationEntity> results = updateWithPending(tableId, res);		
		List<OpData> ops = new ArrayList<OpData>(results.size());
		for (OperationEntity e : results) {
			Operation inst = Utils.newInstance(e.className);
			OpData op = new OpData(e.data, inst);
			ops.add(op);
		}
		return ops;
	}

	public Set<OperationEntity> updateWithPending(String tableId, List<OperationEntity> res) {
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
	public String process(Operation op) {
		Bastogne bastogne = new Bastogne();
		bastogne.setMapInfo(loadMapInfo());
		op.game = bastogne;
		op.serverExecute();
		OperationEntity e = new OperationEntity();
		e.sessionId = getTableId();
		e.data = op.encode();
		e.className = op.getClass().getName();
		e.timestamp = new Date();
		ofy().save().entity(e);
		
		String lastKey = (String) cache.get(e.sessionId);
		CacheEntry c = new CacheEntry();
		c.nextKey = lastKey;
		c.value = e;
		String newKey = e.sessionId+e.timestamp;
		cache.put(newKey, c);
		cache.put(e.sessionId, newKey);
		long tableId = Long.parseLong(getTableId());
		notify.notifyListeners(tableId, op, getUser());
		return e.data;
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
