package earl.client;

import com.google.gwt.appengine.channel.client.SocketError;
import com.google.gwt.appengine.channel.client.SocketListener;
import com.google.gwt.user.client.Window;

import earl.client.bastogne.op.Move;
import earl.client.bastogne.op.OpponentConnected;
import earl.client.data.Board;
import earl.client.ex.EarlException;
import earl.client.op.EarlDisplay;
import earl.client.op.Operation;

public final class NotificationListener implements SocketListener {
	private Board board;
	private final EarlDisplay display;

	public NotificationListener(Board board, EarlDisplay display) {
		super();
		this.board = board;
		this.display = display;
	}

	@Override
	public void onOpen() {
		ClientEngine.log("Channel API opened");
	}

	@Override
	public void onMessage(String message) {
		int idx = message.indexOf(':');
		String type = message.substring(0, idx);
		try {
			Operation op = getOperation(type);
			op.decode(board, message.substring(idx + 1));
			op.clientExecute();
			op.draw(display);
		} catch (Exception e) {
			ClientEngine.log("[Channel API] " + e.toString());			
		}
		ClientEngine.log("[Channel API] " + message);
	}

	private Operation getOperation(String type) {
		if (Move.class.getName().equals(type)) {
			return new Move();
		} else if (OpponentConnected.class.getName().equals(type)) {
			return new OpponentConnected();
		} else {
			throw new EarlException("Unknow operation type:" + type);
		}
	}

	@Override
	public void onError(SocketError error) {
		Window.alert("Channel API error:" + error + ":" + error.getCode() + ":" + error.getDescription());
	}

	@Override
	public void onClose() {
		ClientEngine.log("Channel API closed");
	}
}