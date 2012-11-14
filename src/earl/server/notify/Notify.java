package earl.server.notify;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;

import earl.manager.Persistence;
import earl.manager.PersistenceFactory;

public class Notify {
	private Persistence persistence = PersistenceFactory.get();

	public void notifyListeners(String tableId, String msg) {
		ChannelService channelService = ChannelServiceFactory.getChannelService();
		List<TableListener> listeners = persistence.getListeners(tableId);
		removeObsoleteListeners(listeners);
		for (TableListener listener : listeners) {
			String clientId = listener.getClientId();
			ChannelMessage message = new ChannelMessage(clientId, msg);
			System.out.println("sending to " + clientId);
			channelService.sendMessage(message);
		}
	}

	private void removeObsoleteListeners(List<TableListener> listeners) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR, -2);
		Date timeout = cal.getTime();
		for (TableListener listener : listeners) {
			Date created = listener.getCreated();
			if (created.compareTo(timeout) <= 0) {
				persistence.delete(listener);
			} 
		}
	}
}
