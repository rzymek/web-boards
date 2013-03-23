package webboards.client.utils;

import com.google.gwt.dom.client.Document;
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
	
	public static native void write(Document doc, String html) /*-{
		doc.write(html);
	}-*/;
}
