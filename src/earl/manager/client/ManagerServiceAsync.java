package earl.manager.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ManagerServiceAsync {

	void startGame(AsyncCallback<Void> callback);

	void getUserTables(AsyncCallback<List<String>> callback);

	void getInvitations(AsyncCallback<List<String>> callback);

}
