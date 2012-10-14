package earl.engine.client;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class EarlCallback<T> implements AsyncCallback<T> {

	@Override
	public void onFailure(Throwable caught) {
		Window.alert(caught.toString());
	}
	
	@Override
	public void onSuccess(T result) {
	}

}
