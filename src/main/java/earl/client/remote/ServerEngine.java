package earl.client.remote;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import earl.client.data.GameInfo;
import earl.client.ops.Operation;

@RemoteServiceRelativePath("engine")
public interface ServerEngine extends RemoteService {
	GameInfo getState(String tableId) throws Exception;
	Operation process(Operation op) throws Exception;
	void join(String tableId) throws Exception;
	GameInfo undo(long table) throws Exception;
}
