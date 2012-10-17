package earl.engine.client;

import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface EngineServiceAsync {

	void updateLocation(String unitId, String hexId,
			AsyncCallback<Void> callback);

	void getUnits(AsyncCallback<Map<String, String>> callback);

	void openChannel(AsyncCallback<String> callback);

}
