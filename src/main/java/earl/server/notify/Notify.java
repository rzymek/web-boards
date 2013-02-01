package earl.server.notify;

import no.eirikb.gwtchannelapi.server.ChannelServer;

import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;

import earl.client.OperationMessage;
import earl.client.ops.Operation;
import earl.client.ops.generic.OpponentConnected;
import earl.server.entity.Table;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class Notify {

	public void notifyListeners(long tableId, Operation op, String fromUser) {
		Table table = ofy().load().type(Table.class).id(tableId).get();
		String recipient = getRecipient(fromUser, table);
		if (recipient == null) {
			return;
		}
		String clientId = getClientId(tableId, recipient);
		OperationMessage message = new OperationMessage();
		message.op = op;
		ChannelServer.send(clientId, message);
	}

	private String getRecipient(String fromUser, Table table) {
		if (fromUser.equals(table.player1)) {
			return table.player2;
		} else if (fromUser.equals(table.player2)) {
			return table.player1;
		} else {
			return null;
		}
	}

	public String openChannel(long tableId, String user) {
		ChannelService service = ChannelServiceFactory.getChannelService();
		String clientId = getClientId(tableId, user);
		String token = service.createChannel(clientId);
		System.out.println("client connected: " + clientId + " token=" + token);
		notifyListeners(tableId, new OpponentConnected(user), user);
		return token;
	}

	private String getClientId(long tableId, String user) {
		String clientId = tableId + "-" + user;
		return clientId;
	}
}
