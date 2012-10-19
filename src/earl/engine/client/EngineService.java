package earl.engine.client;

import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("engine")
public interface EngineService extends RemoteService {
	void updateLocation(String unitId, String hexId);

	Map<String, String> getUnits();
	
	String openChannel();
	
	int roll(int d, int sides);
}
