package earl.client.utils;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

public class DOMUtils {

	public static native void addClickHandler(JavaScriptObject element, ClickHandler clickHandler) /*-{
		element.addEventListener('click', function(evt) {
			var clickEvent = @earl.client.utils.DOMUtils::createClickEvent(Lcom/google/gwt/dom/client/NativeEvent;)(evt);
			clickHandler.@com.google.gwt.event.dom.client.ClickHandler::onClick(Lcom/google/gwt/event/dom/client/ClickEvent;)(clickEvent);
		});
	}-*/;
	
	public static ClickEvent createClickEvent(NativeEvent evt) {
		return new ClickEventImpl(evt);
	}; 
}
