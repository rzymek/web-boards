package earl.engine.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.io.IOUtils;

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
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
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
	public static void notifyListeners(String tableId, String msg) {
		ChannelService channelService = ChannelServiceFactory.getChannelService();
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		Filter filter = new Query.FilterPredicate("table", FilterOperator.EQUAL, tableId);
		Query query = new Query("listener").setFilter(filter);
		Iterable<Entity> results = ds.prepare(query).asIterable();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR, -2);
		Date timeout = cal.getTime();
		List<Key> delete = new ArrayList<Key>();
		for (Entity entity : results) {
			Date created = (Date) entity.getProperty("created");
			if(created.compareTo(timeout) <= 0) {
				delete.add(entity.getKey());
			}else{
				String clientId = entity.getKey().getName();
				ChannelMessage message = new ChannelMessage(clientId, msg);
				System.out.println("sending to "+clientId);
				channelService.sendMessage(message);
			}
		}
		System.out.println("notifyListeners: done");		
		ds.delete(delete);
	}

	
	private String joinChannel() {
		String tableId = getTableId();
		String user = getCurrentUser();
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		String clientId = UUID.randomUUID().toString();
		Entity listener = new Entity(KeyFactory.createKey("listener", clientId));
		listener.setProperty("table", tableId);
		listener.setProperty("user", user);
		listener.setProperty("created", new Date());
		ds.put(listener);
		ChannelService service= ChannelServiceFactory.getChannelService();
		String token = service.createChannel(clientId);
		System.out.println("client connected: "+clientId+" token="+token);
		notifyListeners(tableId, "Client connected");
		return token;
	}
	
	@Override
	public int roll(int d, int sides) {
		String tableId = getTableId();
		int sum = 0;
		for(int i=0;i<d;++i) {
			sum += random.nextInt(sides)+1;
		}
		notifyListeners(tableId, d+"d"+sides+" = "+sum);
		return sum;
	}
	
	@Override
	public GameInfo connect() {
		GameInfo info = new GameInfo();
		info.channelToken = joinChannel();
		info.units = getUnits();	
		info.sides = getSides();
		return info;
	}

	private Map<String,String> getSides() {
		try {
			Map<String, String> sides = new HashMap<String, String>();
			InputStream in = getServletContext().getResourceAsStream("/bastogne/units/units.txt");
			@SuppressWarnings("unchecked")
			List<String> lines = IOUtils.readLines(in);
			for (String line : lines) {
				if(line.trim().isEmpty() || line.startsWith("#")) {
					continue;
				}
				String[] split = line.split("=");
				if(split.length != 2) {
					continue;
				}
				sides.put(split[0], split[1]);
			}
			return sides;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
}
