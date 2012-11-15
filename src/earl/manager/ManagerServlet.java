package earl.manager;

import java.io.IOException;
import java.security.Principal;
import java.util.Collection;
import java.util.Collections;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.channel.ChannelPresence;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;

import earl.client.games.Bastogne;
import earl.client.games.BastogneSide;


public class ManagerServlet extends HttpServlet {
	private final static Logger log = Logger.getLogger(ManagerServlet.class.getName());
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String pathInfo = req.getPathInfo();
		String user = getCurrentUser(req);
		GameManager manager = GameManager.get();
		Cookie cookie = new Cookie("earl.user", user);
		resp.addCookie(cookie);
		if ("/start".equals(pathInfo)) {
			Bastogne game = new Bastogne();
			game.setupScenarion52();
			game.setPlayer(BastogneSide.US, user);
			String tableId = manager.start(game);
			resp.sendRedirect("/bastogne/?table=" + tableId);
		} else if ("/join".equals(pathInfo)) {
			String tableId = req.getParameter("table");
			manager.join(tableId, user);
			resp.sendRedirect("/bastogne/?table=" + tableId);
		} else {
			Collection<Table> started = manager.getParticipatingIn(user);
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
			GameManager.get().deleteListener(clientId);
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