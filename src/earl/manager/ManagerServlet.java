package earl.manager;

import java.io.IOException;
import java.net.URLEncoder;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

import earl.engine.client.Table;

public class ManagerServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String pathInfo = req.getPathInfo();
		if("/start".equals(pathInfo)){
			String username = getCurrentUser(req);
			DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
			String name = UUID.randomUUID().toString();
			Key key = KeyFactory.createKey("table", name);
			Entity table = new Entity(key);
			table.setProperty("created", new Date());
			table.setProperty("player1", username);
			ds.put(table);
			String tableId = table.getKey().getName();
			resp.sendRedirect("/?table="+tableId+copyParams(req));
		}else if("/join".equals(pathInfo)) {
			try {
				String username = getCurrentUser(req);
				DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
				String tableId = req.getParameter("table");
				Entity table = ds.get(KeyFactory.createKey("table", tableId));
				table.setProperty("player2", username);
				ds.put(table);
				resp.sendRedirect("/?table="+tableId+copyParams(req));
			}catch (Exception e) {
				throw new AssertionError(e);
			}
		}else{
			List<Table> started = getStarted(req);
			List<Table> invitations = getInvitations(req);
			req.setAttribute("earl.started", started);
			req.setAttribute("earl.invitations", invitations);
			req.getRequestDispatcher("/WEB-INF/jsp/manage.jsp").forward(req, resp);
		}
	}

	@SuppressWarnings("deprecation")
	public static String copyParams(HttpServletRequest req) {
		@SuppressWarnings("unchecked")
		Map<String,String[]> params = req.getParameterMap();
		if(params.isEmpty()) {
			return "";
		}
		StringBuilder buf = new StringBuilder();
		for (Entry<String, String[]> p : params.entrySet()) {
			String key = URLEncoder.encode(p.getKey());
			for (String value : p.getValue()) {
				String v = URLEncoder.encode(value);
				buf.append("&").append(key).append("=").append(v);				
			}
		}
		return buf.toString();
	}

	protected String getCurrentUser(HttpServletRequest req) {
		Principal principal = req.getUserPrincipal();
		if (principal == null) {
			throw new SecurityException("Not logged in.");
		}
		return principal.getName();
	}

	public List<Table> getStarted(HttpServletRequest req) {
		String username = getCurrentUser(req);
		Filter filter= new FilterPredicate("player1", FilterOperator.EQUAL, username);
		Query query = new Query("table").setFilter(filter);
		return getTables(query, req);
	}

	public List<Table> getInvitations(HttpServletRequest req) {
		String username = getCurrentUser(req);
		Query q = new Query("table");
		Filter filter = new FilterPredicate("player1", FilterOperator.NOT_EQUAL, username);
		q.setFilter(filter);
		return getTables(q, req);
	}

	protected List<Table> getTables(Query query, HttpServletRequest req) {
		String username = getCurrentUser(req);
		List<Table> result = new ArrayList<Table>();
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		Iterable<Entity> tables = ds.prepare(query).asIterable();
		for (Entity entity : tables) {
			Date started = (Date) entity.getProperty("created");
			String player1 = (String) entity.getProperty("player1");
			String player2 = (String) entity.getProperty("player2");
			Table table = new Table();
			if (username.equals(player1)) {
				table.opponent = player2;
			} else {
				table.opponent = player1;
			}
			table.id = entity.getKey().getName();
			table.started = started;
			result.add(table);
		}
		return result;
	}

}
