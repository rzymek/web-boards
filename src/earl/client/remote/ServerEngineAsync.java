package earl.client.remote;

import com.google.gwt.user.client.rpc.AsyncCallback;

import earl.client.data.Counter;
import earl.client.data.GameInfo;
import earl.client.data.Hex;

public interface ServerEngineAsync {
	void getState(String tableId, AsyncCallback<GameInfo> callback);

	void roll(int dice, int sides, AsyncCallback<Integer> callback);

	void counterChanged(Counter piece, AsyncCallback<Void> callback);

	void counterMoved(Counter counter, Hex from, Hex to, AsyncCallback<Void> callback);
}
