package earl.engine.server;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import earl.engine.client.EngineService;
import earl.engine.client.data.GameInfo;

public class EngineServiceImpl extends RemoteServiceServlet implements
		EngineService {
	private static Random random = new Random();
	
	private static final long serialVersionUID = 1L;
	
	protected String getCurrentUser() {
		Principal principal = getThreadLocalRequest().getUserPrincipal();
		if(principal == null) {
			throw new SecurityException("Not logged in.");
		}
		return principal.getName();
	}

	@Override
	public void updateLocation(String unitId, String hexId)  {
		try {
			DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
			Key tableKey = getTableKey();
			Entity unit = new Entity("unit", unitId, tableKey);
			unit.setProperty("position", hexId);
			ds.put(unit);			
			String tableId = tableKey.getName();
			String msg = unitId+":"+hexId;
			notifyListeners(tableId, msg);
		}catch (Exception e) {
	        throw new AssertionError(e);
		}
	}

	private static void notifyListeners(String tableId, String msg) {
		ChannelService channelService = ChannelServiceFactory.getChannelService();
		String clientId = tableId;
		ChannelMessage message = new ChannelMessage(clientId, msg);
		channelService.sendMessage(message);
	}

	private String getOponent(Entity table) {
		String player1 = (String) table.getProperty("player1");
		String player2 = (String) table.getProperty("player2");
		String username = getCurrentUser();
		if (username.equals(player1)) {
			return player2;
		} else {
			return player1;
		}
	}

	protected Key getTableKey() {
		String tableId = getTableId();
		Key tableKey = KeyFactory.createKey("table", tableId);
		return tableKey;
	}

	protected Entity getTable(DatastoreService ds) throws EntityNotFoundException {
		String tableId = getTableId();
		Entity table = ds.get(KeyFactory.createKey("table", tableId));
		return table;
	}

	protected String getTableId() {
		String referer = getThreadLocalRequest().getHeader("referer");
		Map<String, List<String>> queryParams = getQueryParams(referer);
		String tableId = queryParams.get("table").get(0);
		return tableId;
	}
	public static Map<String, List<String>> getQueryParams(String url) {
	    try {
	        Map<String, List<String>> params = new HashMap<String, List<String>>();
	        String[] urlParts = url.split("\\?");
	        if (urlParts.length > 1) {
	            String query = urlParts[1];
	            for (String param : query.split("&")) {
	                String[] pair = param.split("=");
	                String key = URLDecoder.decode(pair[0], "UTF-8");
	                String value = "";
	                if (pair.length > 1) {
	                    value = URLDecoder.decode(pair[1], "UTF-8");
	                }

	                List<String> values = params.get(key);
	                if (values == null) {
	                    values = new ArrayList<String>();
	                    params.put(key, values);
	                }
	                values.add(value);
	            }
	        }

	        return params;
	    } catch (UnsupportedEncodingException ex) {
	        throw new AssertionError(ex);
	    }
	}

	private Map<String, String> getUnits() {
		try {
			DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
			Query q = new Query("unit").setAncestor(getTableKey());
			Iterable<Entity> results = ds.prepare(q).asIterable();
			Map<String, String> map = new HashMap<String, String>();
			for (Entity entity : results) {
				String unitId = entity.getKey().getName();
				String hexId = (String) entity.getProperty("position");
				map.put(unitId, hexId);
			}
			return map;
		}catch (Exception e) {
			throw new AssertionError(e);
		}
	}
	
	private String joinChannel() {
		String tableId = getTableId();
		ChannelService service= ChannelServiceFactory.getChannelService();
		notifyListeners(tableId, "client connected");
		String token = service.createChannel(tableId);
		return token;
	}
	
	@Override
	public int roll(int d, int sides) {
		String tableId = getTableId();
		int sum = 0;
		for(int i=0;i<d;++i) {
			sum += random.nextInt(sides)+1;
		}
		notifyListeners(tableId, d+"D"+sides+" = ");
		return sum;
	}
	
	@Override
	public GameInfo connect() {
		GameInfo info = new GameInfo();
		info.channelToken = joinChannel();
		info.units = getUnits();
		return info;
	}
	
}
