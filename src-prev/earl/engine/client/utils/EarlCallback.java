package earl.engine.client.utils;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

import earl.engine.client.Earl;

public class EarlCallback<T> implements AsyncCallback<T> {

	@Override
	public void onFailure(Throwable caught) {
		Earl.log(caught.toString());
		Window.alert(caught.toString());
	}
	
	@Override
	public void onSuccess(T result) {
	}

}
