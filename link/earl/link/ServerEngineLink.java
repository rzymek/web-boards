package earl.link;

import com.google.gwt.user.client.rpc.AsyncCallback;

import earl.client.data.Counter;
import earl.client.data.GameInfo;
import earl.client.data.Hex;
import earl.client.remote.ServerEngineAsync;
import earl.server.ServerEngineImpl;

/**
 * Only purpose: easier source navigation in eclipse
 */
public class ServerEngineLink implements ServerEngineAsync {
	private ServerEngineImpl impl;

	@Override
	public void getState(String tableId, AsyncCallback<GameInfo> callback) {
		impl.getState(tableId);
	}

	@Override
	public void roll(int dice, int sides, AsyncCallback<Integer> callback) {
		impl.roll(dice, sides);
	}

	@Override
	public void counterChanged(Counter piece, AsyncCallback<Void> callback) {
		impl.counterChanged(piece);
	}

	@Override
	public void counterMoved(Counter counter, Hex from, Hex to, AsyncCallback<Void> callback) {
		impl.counterMoved(counter, from, to);
	}
}
