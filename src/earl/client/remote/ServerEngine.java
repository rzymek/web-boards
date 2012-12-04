package earl.client.remote;

import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import earl.client.data.GameInfo;
import earl.client.display.GameChangeListener;
import earl.client.op.Operation;

@RemoteServiceRelativePath("engine")
public interface ServerEngine extends RemoteService, GameChangeListener {
	GameInfo getState(String tableId);
	int roll(int dice, int sides);
	void chat(String text);
	void setAttacks(Map<String, String> attackHexes);
	void process(Operation op);
}
