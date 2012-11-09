package earl.client.utils;

import com.google.gwt.user.client.rpc.AsyncCallback;

import earl.client.ClientEngine;

public class AbstractCallback<T> implements AsyncCallback<T> {
	@Override
	public void onFailure(Throwable caught) {
		ClientEngine.log(caught.toString());
	}

	@Override
	public void onSuccess(T result) {
	}

}
