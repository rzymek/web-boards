package earl.client.remote;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import earl.client.data.GameInfo;
import earl.client.ops.Operation;

@RemoteServiceRelativePath("engine")
public interface ServerEngine extends RemoteService {
	GameInfo getState(String tableId);
	Operation process(Operation op);
	void join(String tableId);
}
