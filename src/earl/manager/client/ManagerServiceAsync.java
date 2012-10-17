package earl.manager.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import earl.engine.client.Table;

public interface ManagerServiceAsync {

	void startGame(AsyncCallback<String> callback);

	void getUserTables(AsyncCallback<List<Table>> callback);

	void getInvitations(AsyncCallback<List<Table>> callback);

}
