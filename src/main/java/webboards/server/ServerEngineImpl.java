package webboards.server;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import webboards.client.data.Board;
import webboards.client.data.GameInfo;
import webboards.client.data.Side;
import webboards.client.ex.EarlException;
import webboards.client.ex.EarlServerException;
import webboards.client.games.scs.bastogne.Bastogne;
import webboards.client.games.scs.bastogne.BastogneSide;
import webboards.client.games.scs.bastogne.scenarios.BattleForLongvilly;
import webboards.client.ops.Operation;
import webboards.client.ops.ServerContext;
import webboards.client.remote.ServerEngine;
import webboards.server.entity.OpCount;
import webboards.server.entity.OperationEntity;
import webboards.server.entity.Player;
import webboards.server.entity.Table;
import webboards.server.entity.TableSearch;
import webboards.server.notify.Notify;
import webboards.server.utils.HttpUtils;
import webboards.server.utils.ServerUtils;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Work;
import com.googlecode.objectify.cmd.LoadType;
import com.googlecode.objectify.cmd.Query;

public class ServerEngineImpl extends RemoteServiceServlet implements ServerEngine {
	private static final Logger log = Logger.getLogger(ServerEngineImpl.class.getName());

	private static final long serialVersionUID = 1L;
	private transient static final Notify notify = new Notify();
	private transient MemcacheService memcache = MemcacheServiceFactory.getMemcacheService();

	@Override
	protected void doUnexpectedFailure(Throwable e) {
		log.log(Level.SEVERE, "UnexpectedFailure", e);
		super.doUnexpectedFailure(e);
	}

	@Override
	public GameInfo getState(long tableId) {
		Table table = getTable(tableId);
		GameInfo info = new GameInfo();
		try {
			String user = getUser();
			List<Player> list = getPlayers(table);
			Player player = getPlayer(list, user);
			if(player == null) {
				List<Side> available = new ArrayList<Side>(Arrays.asList(table.game.getSides()));
				for (Player p : list) {
					available.remove(p.side);
				}
				if(!available.isEmpty()) {
					info.joinAs = available.get(0);
				}
			}else{
				info.side = player.side;
				info.channelToken = player.channelToken; 
			}
			info.game = table.game;
			info.scenario = table.scenario;
			info.ops = loadOps(table);
		} catch (RuntimeException ex) {
			ex.printStackTrace();
			throw ex;
		}
		return info;
	}

	private List<Player> getPlayers(Table table) {
		return ofy().load().type(Player.class).ancestor(table).list();
	}

	public List<Operation> loadOps(Table table) {
		return unwrap(loadOpEntities(table));
	}

	public List<Operation> unwrap(Collection<OperationEntity> res) {
		List<Operation> results = new ArrayList<Operation>();
		for (OperationEntity operationEntity : res) {
			Operation op = ServerUtils.deserialize(operationEntity.data);
			results.add(op);
		}
		return results;
	}

	public List<OperationEntity> loadOpEntities(Table table) {
		//@formatter:off
		return ofy().load()
				.type(OperationEntity.class)
				.ancestor(table)
				.order("timestamp")
				.list();
		//@formatter:on
	}

	private String getUser() {
		Principal principal = getThreadLocalRequest().getUserPrincipal();
		if (principal == null) {
			throw new SecurityException("Not logged in.");
		}
		log.finest("user: " + principal.getName());
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
	public Operation process(final Operation op) {
		return ofy().transact(new Work<Operation>() {
			@Override
			public Operation run() {
				final String tableId = getTableId();
				final long tid = Long.parseLong(tableId);
				Table table = getTable(tid);
				OpCount opCount = ofy().load().type(OpCount.class).ancestor(table).first().get();
				if(opCount == null) {
					opCount = new OpCount(table);
				}
				if(op.index != opCount.count()) {
					throw new EarlException("Optimistic lock: "+op.index+" != "+opCount.count());
				}				
				log.fine("opCount="+opCount);

				final String user = getUser();
				final Side side = getPlayer(getPlayers(table), user).side;
				Board board = getCurrentBoard(tid);

				op.updateBoard(board);
				op.serverExecute(new ServerContext(board));

				OperationEntity e = new OperationEntity(table);
				e.author = side.toString();
				e.data = ServerUtils.serialize(op);
				e.className = op.getClass().getName();
				e.timestamp = new Date();
				e.adebug = ServerUtils.describe(op);

				opCount.incement();
				ofy().save().entities(e, opCount);
				memcache.put("game#" + tid, board);
				notify.notifyListeners(getTable(tid), op, side);
				log.info("Executed " + op);
				return op;
			}
		});
	}

	private Board getCurrentBoard(long tid) {
		Board board = (Board) memcache.get("game#" + tid);
		if (board != null) {
			log.fine(tid + " found in memcache.");
			return board;
		}
		Table table = getTable(tid);
		board = table.game.start(table.scenario);
		List<Operation> ops = loadOps(tid);
		ServerContext ctx = new ServerContext(board);
		for (Operation op : ops) {
			log.fine(tid + " executing: " + op);
			op.updateBoard(board);
			op.serverExecute(ctx);
		}
		log.fine(tid + " from db updated.");
		return board;
	}

	private List<Operation> loadOps(long tid) {
		LoadType<OperationEntity> load = ofy().load().type(OperationEntity.class);
		Query<OperationEntity> query = load.ancestor(Key.create(Table.class, tid));
		List<OperationEntity> opEnts = query.order("timestamp").list();
		return unwrap(opEnts);
	}

	private Table getTable(long tableId) {
		Table table = ofy().load().type(Table.class).id(tableId).get();
		if (table == null) {
			throw new EarlServerException("Invalid table id=" + tableId);
		}
		log.fine(tableId + " found in db. ");
		return table;
	}

	private Player getPlayer(List<Player> list, String user) {
		if(user == null){
			return null;
		}
		for (Player player : list) {
			if(user.equals(player.user)){
				return player;
			}
		}
		return null;
	}

	@Override
	public void join(final long tableId, final Side side) {
		ofy().transact(new Work<Table>() {
			@Override
			public Table run() {
				Table table = getTable(tableId);
				Player player = new Player(table, getUser(), side);
				player.channelToken = notify.openChannel(table, side);
				TableSearch ts = ofy().transactionless().load().type(TableSearch.class).id(table.id).get();
				ts.join(player, table.game.getSides().length);
				ofy().save().entities(player);
				ofy().transactionless().save().entities(ts);
				return table;
			}
		});
	}

	public static Long create(final String user, final String sideName) {
		return ofy().transact(new Work<Long>() {
			@Override
			public Long run() {
				BastogneSide side = BastogneSide.valueOf(sideName);
				Table table = new Table();
				table.game = new Bastogne();
				table.scenario = new BattleForLongvilly();
				ofy().save().entity(table).now();

				Player player = new Player(table, user, side);
				player.channelToken = notify.openChannel(table, side);
				TableSearch ts = new TableSearch(table);
				ts.join(player, table.game.getSides().length);
				ofy().save().entities(player);
				ofy().transactionless().save().entities(ts);
				return table.id;
			}
		});
	}
}
