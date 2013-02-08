package webboards.client.remote;

import webboards.client.data.GameInfo;
import webboards.client.ops.Operation;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ServerEngineAsync {
	void getState(String tableId, AsyncCallback<GameInfo> callback);
	void process(Operation op, AsyncCallback<Operation> abstractCallback);
	void join(String tableId, AsyncCallback<Void> abstractCallback);
	void undo(long table, AsyncCallback<GameInfo> callback);
}
