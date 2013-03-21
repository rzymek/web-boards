package webboards.client.remote;

import webboards.client.data.GameInfo;
import webboards.client.data.Side;
import webboards.client.ops.Operation;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ServerEngineAsync {
	void getState(long tableId, AsyncCallback<GameInfo> callback);
	void process(Operation op, AsyncCallback<Operation> abstractCallback);
	void join(long tableId, Side side, AsyncCallback<Void> abstractCallback);
}
