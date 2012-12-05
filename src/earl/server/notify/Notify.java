package earl.server.notify;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;

import earl.server.Op;

public class Notify {

	public void notifyListeners(String tableId, Op roll) {
		ChannelService channelService = ChannelServiceFactory.getChannelService();
		List<TableListener> listeners = new ArrayList<TableListener>();//TODO
		removeObsoleteListeners(listeners);
		for (TableListener listener : listeners) {
			String clientId = listener.getClientId();
			ChannelMessage message = new ChannelMessage(clientId, roll.toMessage());
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
//				TODO: persistence.delete(listener);
				
			} 
		}
	}

	public String openChannel(String tableId, String user) {
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
		notifyListeners(tableId, new Op(Op.Type.CONNECTED, user));
		return token;
	}
}
