package webboards.server.notify;

import webboards.client.OperationMessage;
import webboards.client.ops.Operation;
import webboards.server.entity.Table;

import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;

public class Notify {

	public void notifyListeners(Table table, Operation op, String fromUser) {
		String recipient = getRecipient(fromUser, table);
		if (recipient == null) {
			return;
		}
		String clientId = getClientId(table.id, recipient);
		OperationMessage message = new OperationMessage();
		message.op = op;
//		ChannelServer.send(clientId, message);
	}

	private String getRecipient(String fromUser, Table table) {
		int pos = table.getPlayerPosition(fromUser);
		return table.players[(pos + 1) % table.players.length];
	}

	public String openChannel(Table table) {
		ChannelService service = ChannelServiceFactory.getChannelService();
//		String token = service.createChannel(table.id);
//		notifyListeners(table, new OpponentConnected(user), user);
		return null;
	}

	private String getClientId(long tableId, String user) {
		return tableId + "-" + user;
	}
}
