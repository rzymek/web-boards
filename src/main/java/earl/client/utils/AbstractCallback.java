package earl.client.utils;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

import earl.client.ClientEngine;

public class AbstractCallback<T> implements AsyncCallback<T> {
	@Override
	public void onFailure(Throwable caught) {
		handle(caught);
	}

	public static void handle(Throwable caught) {
		StringBuilder buf = new StringBuilder();
		for(;;) {
			buf.append(caught.toString()).append("\n");
			if(caught.getCause() == caught || caught.getCause() == null) {
				break;
			}
			caught = caught.getCause();
		}
		buf.append("Stack:\n");
		StackTraceElement[] stackTrace = caught.getStackTrace();
		for (StackTraceElement e : stackTrace) {
			buf.append(e).append("\n");
		}
		Window.alert(buf.toString());
		ClientEngine.log(buf.toString());
	}

	@Override
	public void onSuccess(T result) {
	}

}
