package earl.engine.client;

import com.google.gwt.appengine.channel.client.SocketError;
import com.google.gwt.appengine.channel.client.SocketListener;
import com.google.gwt.user.client.Window;

public class EarlSocketListener implements SocketListener {

	private final Earl earl;

	public EarlSocketListener(Earl earl) {
		this.earl = earl;
	}

	@Override
	public void onOpen() {
		Window.alert("Channel opened");
	}

	@Override
	public void onMessage(String message) {
		String[] split = message.split(":");
		String unitId = split[0].trim();
		String hexId = split[1].trim();
		earl.moveToHex(unitId, hexId);
	}

	@Override
	public void onError(SocketError error) {
		Window.alert(error.getDescription());
	}

	@Override
	public void onClose() {
		Window.alert("Channel closed");
	}

}
