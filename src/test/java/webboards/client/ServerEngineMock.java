package webboards.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import webboards.client.data.GameInfo;
import webboards.client.data.Side;
import webboards.client.ex.ConcurrentOpException;
import webboards.client.ops.Operation;
import webboards.client.remote.ServerEngineAsync;

public class ServerEngineMock implements ServerEngineAsync {
	boolean doThrow = true;

	@Override
	public void getState(long tableId, AsyncCallback<GameInfo> callback) {
	}

	@Override
	public void process(Operation op, AsyncCallback<Operation> abstractCallback) {
		if (doThrow) {
			doThrow = false;
			ConcurrentOpException ex = new ConcurrentOpException("msg", op);
			abstractCallback.onFailure(ex);
		} else {
			doThrow = false;
		}
	}

	@Override
	public void join(long tableId, Side side, AsyncCallback<Void> abstractCallback) {
	}
}
