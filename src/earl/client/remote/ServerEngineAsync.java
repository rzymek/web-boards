package earl.client.remote;

import com.google.gwt.user.client.rpc.AsyncCallback;

import earl.client.data.Board;

public interface ServerEngineAsync {
	void getState(String tableId, AsyncCallback<Board> callback);
}
