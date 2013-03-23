package webboards.client.remote;

import webboards.client.data.GameInfo;
import webboards.client.data.Side;
import webboards.client.ex.ConcurrentOpException;
import webboards.client.ops.Operation;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("engine")
public interface ServerEngine extends RemoteService {
	GameInfo getState(long tableId);
	Operation process(Operation op) throws ConcurrentOpException;
	void join(long tableId, Side side);
	String reopenInstantNotif();
}
