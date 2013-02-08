package webboards.client.remote;

import webboards.client.data.GameInfo;
import webboards.client.ops.Operation;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("engine")
public interface ServerEngine extends RemoteService {
	GameInfo getState(String tableId);
	Operation process(Operation op);
	void join(String tableId);
	GameInfo undo(long table);
}
