package earl.server.servlet;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.channel.ChannelPresence;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Result;
import com.googlecode.objectify.cmd.Query;

import earl.client.games.scs.bastogne.BastogneSide;
import earl.server.entity.GameState;
import earl.server.entity.OperationEntity;
import earl.server.entity.Table;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class ManagerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static Logger log = Logger.getLogger(ManagerServlet.class.getName());

	@Override
	public void init() throws ServletException {
		ObjectifyService.register(OperationEntity.class);
		ObjectifyService.register(Table.class);
		ObjectifyService.register(GameState.class);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String user = req.getUserPrincipal().getName();
		String sideName = req.getParameter("side");
		if (sideName != null) {
			BastogneSide side = BastogneSide.valueOf(sideName);

			Table table = new Table();
			if (side == BastogneSide.US) {
				table.player1 = user;
			} else if (side == BastogneSide.GE) {
				table.player2 = user;
			}
			Result<Key<Table>> result = ofy().save().entity(table);
			String tableId = String.valueOf(result.now().getId());
			resp.sendRedirect("/bastogne/index.jsp?table=" + tableId);
		} else {
			req.setAttribute("earl.started", getStarted(user));
			req.setAttribute("earl.waiting", getWaitingForOp(user));
			req.setAttribute("earl.invitations", getVacant(user));
			req.getRequestDispatcher("/WEB-INF/jsp/manage.jsp").forward(req, resp);
		}
	}

	private Object getWaitingForOp(String user) {
		Query<Table> query = ofy().load().type(Table.class).limit(15);
		Query<Table> q1 = query.filter("player1 =", user).filter("player2 =", null);
		Query<Table> q2 = query.filter("player2 =", user).filter("player1 =", null);
		List<Table> results = new ArrayList<Table>();
		results.addAll(q1.list());
		results.addAll(q2.list());
		return results;
	}

	public List<Table> getStarted(String user) {
		Query<Table> query = ofy().load().type(Table.class).limit(15);
		Query<Table> q1 = query.filter("player1 =", user).filter("player2 !=", null);
		Query<Table> q2 = query.filter("player2 =", user).filter("player1 !=", null);
		List<Table> results = new ArrayList<Table>();
		results.addAll(q1.list());
		results.addAll(q2.list());
		return results;
	}

	public List<Table> getVacant(String user) {
		Query<Table> query = ofy().load().type(Table.class).limit(15);
		Query<Table> q1 = query.filter("player1 !=", user).filter("player2 =", null);
		Query<Table> q2 = query.filter("player2 !=", user).filter("player1 =", null);
		List<Table> results = new ArrayList<Table>();
		results.addAll(q1.list());
		results.addAll(q2.list());
		return results;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String requestURI = req.getRequestURI();
		if (" /_ah/channel/disconnected/".equals(requestURI)) {
			ChannelService channelService = ChannelServiceFactory.getChannelService();
			ChannelPresence presence = channelService.parsePresence(req);
			String clientId = presence.clientId();
			log.info("disconnected " + clientId);
		}
	}

	protected String getCurrentUser(HttpServletRequest req) {
		Principal principal = req.getUserPrincipal();
		if (principal == null) {
			throw new SecurityException("Not logged in.");
		}
		return principal.getName();
	}
}