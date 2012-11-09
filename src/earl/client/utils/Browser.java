package earl.client.utils;

import com.google.gwt.dom.client.Element;

public class Browser {
	public static native void console(Object s) /*-{
		if (console) {
			console.log(s);
		}
	}-*/;

	public native static Element createTextNode(String text) /*-{
		return $doc.createTextNode(text);
	}-*/;
}
