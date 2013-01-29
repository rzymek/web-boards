package earl.client.remote;

import com.google.gwt.user.client.rpc.AsyncCallback;

import earl.client.data.GameInfo;
import earl.client.ops.Operation;

public interface ServerEngineAsync {
	void getState(String tableId, AsyncCallback<GameInfo> callback);
	void process(Operation op, AsyncCallback<Operation> abstractCallback);
	void join(String tableId, AsyncCallback<Void> abstractCallback);
}
