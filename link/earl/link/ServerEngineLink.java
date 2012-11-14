package earl.link;

import com.google.gwt.user.client.rpc.AsyncCallback;

import earl.client.data.Board;
import earl.client.remote.ServerEngineAsync;
import earl.server.ServerEngineImpl;

/**
 * Only purpose: easier source navigation in eclipse
 */
public class ServerEngineLink implements ServerEngineAsync {
	private ServerEngineImpl impl;

	@Override
	public void getState(String tableId, AsyncCallback<Board> callback) {
		impl.getState(tableId);
	}

	@Override
	public void roll(int dice, int sides, AsyncCallback<Integer> callback) {
		impl.roll(dice, sides);
	}
}
