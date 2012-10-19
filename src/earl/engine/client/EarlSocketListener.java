package earl.engine.client;

import com.google.gwt.appengine.channel.client.SocketError;
import com.google.gwt.appengine.channel.client.SocketListener;

public class EarlSocketListener implements SocketListener {

	private final Earl earl;

	public EarlSocketListener(Earl earl) {
		this.earl = earl;
	}

	@Override
	public void onOpen() {
		Earl.log("Channel opened");
	}

	@Override
	public void onMessage(String message) {
		String[] split = message.split(":");
		String unitId = split[0].trim();
		String hexId = split[1].trim();
		earl.moveToHex(unitId, hexId);
		Earl.log("Recevied update: "+unitId+" -> "+hexId);
	}

	@Override
	public void onError(SocketError error) {
		Earl.log("Channel error: "+error.getCode()+":"+error.getDescription());
	}

	@Override
	public void onClose() {
		Earl.log("Channel closed.");
	}

}
