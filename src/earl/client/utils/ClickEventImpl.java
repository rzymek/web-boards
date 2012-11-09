package earl.client.utils;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;

public class ClickEventImpl extends ClickEvent{

	public ClickEventImpl(NativeEvent evt) {
		setNativeEvent(evt);
	}

}
