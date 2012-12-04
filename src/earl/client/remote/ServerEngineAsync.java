package earl.client.remote;

import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

import earl.client.data.Counter;
import earl.client.data.GameInfo;
import earl.client.data.Hex;
import earl.client.op.Operation;

public interface ServerEngineAsync {
	void getState(String tableId, AsyncCallback<GameInfo> callback);

	void roll(int dice, int sides, AsyncCallback<Integer> callback);

	void counterFlipped(Counter piece, AsyncCallback<Void> callback);

	void counterMoved(Counter counter, Hex from, Hex to, AsyncCallback<Void> callback);

	void chat(String text, AsyncCallback<Void> callback);

	void setAttacks(Map<String, String> attackHexes, AsyncCallback<Void> callback);

	void process(Operation op, AsyncCallback<Void> abstractCallback);
}
