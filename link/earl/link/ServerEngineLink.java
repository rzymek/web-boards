package earl.link;

import com.google.gwt.user.client.rpc.AsyncCallback;

import earl.client.data.GameInfo;
import earl.client.op.Operation;
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
	public void process(Operation op, AsyncCallback<String> abstractCallback) {
		impl.process(op);
	}

	@Override
	public void join(String tableId, AsyncCallback<Void> abstractCallback) {
		impl.join(tableId);
	}

}
