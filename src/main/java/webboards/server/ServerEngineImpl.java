package webboards.server;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

import webboards.client.data.Board;
import webboards.client.data.GameInfo;
import webboards.client.data.Side;
import webboards.client.ex.EarlServerException;
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
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Work;
import com.googlecode.objectify.cmd.LoadType;
import com.googlecode.objectify.cmd.Query;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class ServerEngineImpl extends RemoteServiceServlet implements ServerEngine {
	private static final Logger log = Logger.getLogger(ServerEngineImpl.class.getName());

	private static final long serialVersionUID = 1L;
	private transient final Notify notify = new Notify();
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
//			info.channelToken = notify.openChannel(table, getUser());
			String user = getUser();
			int pos = table.getPlayerPosition(user);			
			info.side = table.game.getSides()[pos];
			info.game = table.game;
			info.scenario = table.scenario;
			info.ops = loadOps(table);
		}catch(NoSuchElementException ex) {
			int pos = table.getEmptyPosition();
			info.joinAs = table.game.getSides()[pos];
		}catch(RuntimeException ex){
			ex.printStackTrace();
			throw ex;
		}
		return info;
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
		LoadType<OperationEntity> load = ofy().load().type(OperationEntity.class);
		Query<OperationEntity> query = load.ancestor(table);
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
	public Operation process(final Operation op) {
		return ofy().transact(new Work<Operation>(){
			@Override
			public Operation run() {
				final String tableId = getTableId();
				
				final long tid = Long.parseLong(tableId);
				final String user = getUser();
				final Side side = getSide(tid, user);
				Board board = getCurrentBoard(tid);
						
				op.updateBoard(board);
				op.serverExecute(new ServerContext(board));

				OperationEntity e = new OperationEntity();
				e.table = Key.create(Table.class, tid); 
				e.author = side.toString(); 
				e.data = ServerUtils.serialize(op);
				e.className = op.getClass().getName();
				e.timestamp = new Date();
				e.adebug = ServerUtils.describe(op);

				ofy().save().entity(e);					
				memcache.put("game#"+tid, board);
//				notify.notifyListeners(table, op, user);
				log.info("Executed "+op);
				return op;
			}			
		});
	}

	private Board getCurrentBoard(long tid) {
		Board board= (Board) memcache.get("game#"+tid);
		if(board != null) {
			log.fine(tid+" found in memcache.");
			return board;
		}
		Table table = getTable(tid);
		board = table.game.start(table.scenario);
		List<Operation> ops = loadOps(tid);
		ServerContext ctx = new ServerContext(board);
		for (Operation op : ops) {
			log.fine(tid+" executing: "+op);
			op.updateBoard(board);
			op.serverExecute(ctx);
		}
		log.fine(tid+" from db updated.");
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
		if(table == null){
			throw new EarlServerException("Invalid table id="+tableId);
		}
		log.fine(tableId+" found in db. LastOp:"+table.lastOp);
		return table;
	}

	private Side getSide(long tableId, String user) {
		Table table = getTable(tableId);
		return table.game.getSides()[table.getPlayerPosition(user)];
	}

	@Override
	public void join(final long tableId) {
		ofy().transact(new Work<Table>() {
			@Override
			public Table run() {
				Table table = getTable(tableId);
				int pos = table.getEmptyPosition();
				table.players[pos] = getUser();
				ofy().save().entity(table).now();
				return table;
			}
		});
	}
}
