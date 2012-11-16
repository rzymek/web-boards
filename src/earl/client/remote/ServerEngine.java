package earl.client.remote;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import earl.client.data.GameInfo;
import earl.client.display.GameChangeListener;

@RemoteServiceRelativePath("engine")
public interface ServerEngine extends RemoteService, GameChangeListener {
	GameInfo getState(String tableId);
	int roll(int dice, int sides);
	void chat(String text);
}
