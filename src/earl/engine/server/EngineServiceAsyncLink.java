package earl.engine.server;

import com.google.gwt.user.client.rpc.AsyncCallback;

import earl.engine.client.EngineServiceAsync;
import earl.engine.client.data.GameInfo;

/**
 * Only purpose: easier source navigation in eclipse
 */
public class EngineServiceAsyncLink implements EngineServiceAsync{
	EngineServiceImpl impl;
	@Override
	public void updateLocation(String unitId, String hexId, AsyncCallback<Void> callback) {
		impl.updateLocation(unitId, hexId);
	}

	@Override
	public void roll(int d, int sides, AsyncCallback<Integer> callback) {
		impl.roll(d, sides);
	}

	@Override
	public void connect(AsyncCallback<GameInfo> callback) {
		impl.connect();
	}

}
