package webboards.server;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import webboards.client.data.Game;
import webboards.client.data.GameInfo;
import webboards.client.ex.EarlException;
import webboards.client.ex.EarlServerException;
import webboards.client.games.scs.bastogne.BastogneSide;
import webboards.client.ops.Operation;
import webboards.client.ops.ServerContext;
import webboards.client.remote.ServerEngine;
import webboards.server.entity.OperationEntity;
import webboards.server.entity.Table;
import webboards.server.notify.Notify;
import webboards.server.utils.HttpUtils;
import webboards.server.utils.ServerUtils;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Work;
import com.googlecode.objectify.cmd.LoadType;
import com.googlecode.objectify.cmd.Query;

public class ServerEngineImpl extends RemoteServiceServlet implements ServerEngine {
	private static final Logger log = Logger.getLogger(ServerEngineImpl.class.getName());

	private static final long serialVersionUID = 1L;
	private transient final Notify notify = new Notify();
	private transient MemcacheService memcache = MemcacheServiceFactory.getMemcacheService();

	@Override
	protected void doUnexpectedFailure(Throwable e) {
		e.printStackTrace();
		super.doUnexpectedFailure(e);
	}

	@Override
	public GameInfo getState(String tableId) {
		try {
			long tid = Long.parseLong(tableId);
			Table table = getTable(tid);
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
			info.game = table.state;
			info.ops = loadOps(table);
			return info;
		}catch(RuntimeException ex){
			ex.printStackTrace();
			throw ex;
		}
	}
	
	@Override
	public Game undo(long tid) {
		Table table = getTable(tid);
		List<OperationEntity> opEnts = loadOpEntities(tid, table.stateTimestamp);
		if(!opEnts.isEmpty()) {
			//delete last op
			OperationEntity last = opEnts.get(opEnts.size()-1);
			if(last.timestamp.compareTo(table.stateTimestamp) < 0) {
				throw new EarlException("Can't undo: "+last.timestamp +" < "+ table.stateTimestamp);
			}
			ofy().delete().entity(last).now();
			opEnts.remove(opEnts.size()-1);
		}
		//get the new last author and timestamp:
		if(!opEnts.isEmpty()) {
			OperationEntity lastEnt = opEnts.get(opEnts.size()-1);
			table.stateTimestamp= lastEnt.timestamp;
			table.last = lastEnt.author;
			ofy().save().entity(table);
		}
		return table.state;
	}
	
	public List<Operation> loadOps(Table table) {
		return unwrap(loadOpEntities(table.id, table.stateTimestamp));
	}

	public List<Operation> unwrap(Collection<OperationEntity> res) {
		List<Operation> results = new ArrayList<Operation>();
		for (OperationEntity operationEntity : res) {
			Operation op = ServerUtils.deserialize(operationEntity.data);
			results.add(op);
		}
		return results;
	}

	public List<OperationEntity> loadOpEntities(long tableId, Date timestamp) {
		LoadType<OperationEntity> load = ofy().load().type(OperationEntity.class);
		Query<OperationEntity> query = load.filter("sessionId", String.valueOf(tableId));
		if(timestamp != null) {
			query = query.filter("timestamp >=", timestamp);
		}
		return query.order("timestamp").list();
	}

	private String getUser() {
		Principal principal = getThreadLocalRequest().getUserPrincipal();
		if (principal == null) {
			throw new SecurityException("Not logged in.");
		}
		log.finest("user: "+principal.getName());
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
		Table table = getCurrentTable(tid);
		OperationEntity e = new OperationEntity();
		e.author = getSide(tid, user);
				
		if(table.last == null) {
			//first operation in the game;
			table.last = e.author;
			ofy().save().entity(table);
		}

		op.updateBoard(table.state.getBoard());
		op.serverExecute(new ServerContext(table.state));
		table.lastOp = op.toString();

		e.sessionId = tableId;		
		e.data = ServerUtils.serialize(op);
		e.className = op.getClass().getName();
		e.timestamp = new Date();
		e.adebug = ServerUtils.describe(op);
		
		if(table.last != e.author) {
			table.last = e.author;
			table.stateTimestamp = new Date();
			ofy().save().entities(e, table);
		}else{
			ofy().save().entity(e);
		}
		log.fine("putting table "+tid+" in memcache. LastOp: "+table.lastOp);
		memcache.put("tbl#"+tid, table);
		notify.notifyListeners(table, op, user);
		log.info("Executed "+op);
		return op;
	}

	
	private Game getGame(Table table) {
		MemcacheService memcache = MemcacheServiceFactory.getMemcacheService();
		Game game = (Game) memcache.get("game"+table.id);
		if(game == null) {
			System.out.println(table.id+" game NOT in cache.");
			game = table.state;
			ServerContext ctx = new ServerContext(game);
			List<Operation> ops = loadOps(table);
			for (Operation op : ops) {
				System.out.println(table.id+" executing: "+op);
				op.updateBoard(ctx.game.getBoard());
				op.serverExecute(ctx);
			}
			memcache.put("game"+table.id, game);
		}else{
			System.out.println(table.id+" game taken from cache.");
		}
		return game;
	}

	private Table getCurrentTable(long tid) {
		Table table = (Table) memcache.get("tbl#"+tid);
		if(table != null) {
			log.fine(tid+" found in memcache. LastOp:"+table.lastOp);
			return table;
		}
		table = ServerUtils.clone(getTable(tid));
		log.fine(tid+" not it memcache. Retreiving from DB+update. LastOp:"+table.lastOp);
		List<Operation> ops = loadOps(table);
		ServerContext ctx = new ServerContext(table.state);
		for (Operation op : ops) {
			log.fine(table.id+" executing: "+op);
			op.updateBoard(ctx.game.getBoard());
			op.serverExecute(ctx);
			table.lastOp = op.toString();
		}
		log.fine(tid+" from db updated. LastOp:"+table.lastOp);
		return table;
	}

	private Table getTable(long tableId) {
		Table table = ofy().load().type(Table.class).id(tableId).get();
		if(table == null){
			throw new EarlServerException("Invalid table id="+tableId);
		}
		log.fine(tableId+" found in db. LastOp:"+table.lastOp);
		return table;
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
