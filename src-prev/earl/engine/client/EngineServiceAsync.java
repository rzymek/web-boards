package earl.engine.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import earl.engine.client.data.GameInfo;

public interface EngineServiceAsync {

	void updateLocation(String unitId, String hexId, AsyncCallback<Void> callback);

	void roll(int d, int sides, AsyncCallback<Integer> callback);

	void connect(AsyncCallback<GameInfo> callback);

}
