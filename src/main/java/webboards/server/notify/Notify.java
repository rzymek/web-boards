package webboards.server.notify;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import no.eirikb.gwtchannelapi.server.ChannelServer;
import webboards.client.OperationMessage;
import webboards.client.data.Side;
import webboards.client.ops.Operation;
import webboards.server.entity.Player;
import webboards.server.entity.Table;

import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;

public class Notify {
	private ChannelService service = ChannelServiceFactory.getChannelService();
	private static final Logger log = Logger.getLogger(Notify.class.getName());

	public void notifyListeners(Table table, Operation op, Side side) {
		List<Player> list = ofy().load().type(Player.class).ancestor(table).list();
		for (Player player : list) {
			if (side != null && side.equals(player.side)) {
				continue;
			}
			try {
				String clientId = getClientId(table, player.side);
				ChannelServer.send(clientId, new OperationMessage(op)); 
				log.fine(side + " >>> " + player.channelToken + " <<< " + op.toString());
			} catch (Exception e) {
				log.log(Level.SEVERE, "Unable to send channel message table.id=" + table.id + " side=" + player.side, e);
			}
		}
	}

	public String openChannel(Table table, Side side) {
		try {
			String id = getClientId(table, side);
			String token = service.createChannel(id);
			log.fine("created channel for " + side + ": " + token);
			return token;
		} catch (Exception e) {
			log.log(Level.SEVERE, "Unable to open channel for table=" + table.id + " side=" + side, e);
			return null;
		}
	}

	private String getClientId(Table table, Side side) {
		return table.id + ":" + side;
	}
}
