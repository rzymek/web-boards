package earl.engine.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import earl.engine.client.data.GameInfo;

@RemoteServiceRelativePath("engine")
public interface EngineService extends RemoteService {
	void updateLocation(String unitId, String hexId);

	int roll(int d, int sides);

	GameInfo connect();
}
