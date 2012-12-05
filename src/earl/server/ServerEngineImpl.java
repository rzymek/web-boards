package earl.server;

import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.LoadType;

import earl.client.data.GameInfo;
import earl.client.games.Bastogne;
import earl.client.op.OpData;
import earl.client.op.Operation;
import earl.client.remote.ServerEngine;
import earl.server.ex.EarlServerException;
import earl.server.notify.Notify;
import earl.server.utils.HttpUtils;
import earl.server.utils.Utils;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class ServerEngineImpl extends RemoteServiceServlet implements ServerEngine {
	private static final Logger log = Logger.getLogger(ServerEngineImpl.class.getName());
	
	private static final long serialVersionUID = 1L;
	private final Notify notify = new DisabledNotify();

	@Override
	public GameInfo getState(String tableId) {
		ObjectifyService.register(OperationEntity.class);
		GameInfo info = new GameInfo();
		info.channelToken = notify.openChannel(tableId, getUser());
		info.mapInfo = loadMapInfo();
		LoadType<OperationEntity> load = ObjectifyService.ofy().load().type(OperationEntity.class);
		List<OperationEntity> results = load.filter("sessionId", tableId).order("timestamp").list();
		info.ops = new ArrayList<OpData>(results.size());
		for (OperationEntity e : results) {
			Operation inst = Utils.newInstance(e.className);
			OpData op = new OpData(e.data, inst);
			info.ops.add(op);
		}
		return info;
	}

	private Map<String,String> loadMapInfo() {
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
				info.put((String)entry.getKey(), (String) entry.getValue());
			}
			return info;
		}catch (IOException e) {
			throw new EarlServerException(e);
		}
	}
	
	private String getUser() {
		Principal principal = getThreadLocalRequest().getUserPrincipal();
		if(principal == null) {
//			throw new SecurityException("Not logged in.");
			return null;
		}
		return principal.getName();
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
	public String process(Operation op) {
		op.serverExecute();
		ObjectifyService.register(OperationEntity.class);
		OperationEntity e = new OperationEntity();
		e.sessionId = getTableId();
		e.data = op.encode();
		e.className = op.getClass().getName();
		e.timestamp = new Date();
		ofy().save().entity(e);
		return e.data;
	}
}
