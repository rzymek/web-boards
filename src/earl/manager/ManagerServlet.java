package earl.manager;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.channel.ChannelPresence;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

import earl.client.games.Bastogne;
import earl.client.games.BastogneSide;


public class ManagerServlet extends HttpServlet {
	private final static Logger log = Logger.getLogger(ManagerServlet.class.getName());
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String pathInfo = req.getPathInfo();
		String username = getCurrentUser(req);
		Cookie cookie = new Cookie("earl.user", username);
		resp.addCookie(cookie);
		if ("/start".equals(pathInfo)) {
			GameManager manager = GameManager.get();
			Bastogne game = new Bastogne();
			game.setupScenarion52();
			game.setPlayer(BastogneSide.US, username);
			String tableId = manager.start(game);
			resp.sendRedirect("/bastogne/?table=" + tableId);
		} else if ("/join".equals(pathInfo)) {
			String tableId = req.getParameter("table");
			resp.sendRedirect("/bastogne/?table=" + tableId);
		} else {
			GameManager manager = GameManager.get();
			String user = getCurrentUser(req);
//			Collection<Game> started = manager.getParticipatingIn(user);
//			Collection<Game> invitations = manager.getInvitationsFor(user);
			Collection<Table> started = Collections.emptySet();
			Collection<Table> invitations = Collections.emptySet();
			req.setAttribute("earl.started", started);
			req.setAttribute("earl.invitations", invitations);
			req.getRequestDispatcher("/WEB-INF/jsp/manage.jsp").forward(req, resp);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String requestURI = req.getRequestURI();
		if(" /_ah/channel/disconnected/".equals(requestURI)){
			ChannelService channelService = ChannelServiceFactory.getChannelService();
			ChannelPresence presence = channelService.parsePresence(req);
			String clientId = presence.clientId();
			log.info("disconnected "+clientId);
			DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
			ds.delete(KeyFactory.createKey("listener", clientId));
		}
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
		Filter player1 = new FilterPredicate("player1", FilterOperator.EQUAL, username);
		Filter player2 = new FilterPredicate("player2", FilterOperator.EQUAL, username);
		Query query = new Query("table").setFilter(CompositeFilterOperator.or(player1, player2));
		return getTables(query, req);
	}

	public List<Table> getInvitations(HttpServletRequest req) {
		Query q = new Query("table");
		Filter player1 = new FilterPredicate("player1", FilterOperator.EQUAL, null);
		Filter player2 = new FilterPredicate("player2", FilterOperator.EQUAL, null);
		q.setFilter(CompositeFilterOperator.or(player1, player2));
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


	@SuppressWarnings("deprecation")
	public static String copyParams(HttpServletRequest req) throws UnsupportedEncodingException {
		@SuppressWarnings("unchecked")
		Map<String, String[]> params = req.getParameterMap();
		if (params.isEmpty()) {
			return "";
		}
		StringBuilder buf = new StringBuilder();
		for (Entry<String, String[]> p : params.entrySet()) {
			String key = URLEncoder.encode(p.getKey(),"utf-8");
			for (String value : p.getValue()) {
				String v = URLEncoder.encode(value,"utf-8");
				if (buf.length() > 0) {
					buf.append("&");
				}
				buf.append(key).append("=").append(v);
			}
		}
		return buf.toString();
	}}
