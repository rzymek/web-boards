package earl.client.remote;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import earl.client.data.Board;

@RemoteServiceRelativePath("engine")
public interface ServerEngine extends RemoteService {
	Board getState(String tableId);
}
