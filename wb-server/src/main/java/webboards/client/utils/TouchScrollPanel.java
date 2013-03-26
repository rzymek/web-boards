package webboards.client.utils;
 
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
 
/**
 * @author peter.franza
 */
public class TouchScrollPanel extends ScrollPanel {
 
	private int initialTouchX = -1;
	private int initialTouchY = -1;
	private int initialHorizontalOffset;
	private int initialVerticalOffset;
	private boolean moved = false;
 
	{
		attachTouch(getElement());
	}
 
	public TouchScrollPanel(VerticalPanel body) {
		super(body);
	}
 
	private native void attachTouch(JavaScriptObject ele) /*-{	
		var ref = this;
		ele.ontouchstart = function(evt){
	  		evt.preventDefault();	
	  		ref.@webboards.client.utils.TouchScrollPanel::setInitialTouch(II)(evt.touches[0].screenX, evt.touches[0].screenY);
		}	
		ele.ontouchmove = function(evt){
	  		evt.preventDefault();	
	  		ref.@webboards.client.utils.TouchScrollPanel::doScroll(II)(evt.touches[0].screenX, evt.touches[0].screenY);
		}		
		ele.ontouchend = function(evt){
			evt.preventDefault();
			ref.@webboards.client.utils.TouchScrollPanel::setEndTouch(II)(evt.pageX, evt.pageY);
		}		
	}-*/;
 
	private native void fireClick(int x, int y) /*-{	
		var theTarget = $doc.elementFromPoint(x, y);
		if (theTarget.nodeType == 3) theTarget = theTarget.parentNode;
 
		var theEvent = $doc.createEvent('MouseEvents');
		theEvent.initEvent('click', true, true);
		theTarget.dispatchEvent(theEvent);
	}-*/;
 
 
	private void setInitialTouch(int x, int y) {
		initialVerticalOffset = getVerticalScrollPosition();
		initialHorizontalOffset = getHorizontalScrollPosition();
 
		initialTouchX = x;
		initialTouchY = y;
		moved = false;
 
	}
 
	private void doScroll(int x, int y) {
		if (initialTouchY != -1) {
			moved = true;
			int vDelta = initialTouchY - y;
			int hDelta = initialTouchX - x;
			setVerticalScrollPosition(vDelta + initialVerticalOffset);
			setHorizontalScrollPosition(hDelta + initialHorizontalOffset);
		}
	}
 
	private void setEndTouch(int x, int y) {
		if (!moved) {
			fireClick(x, y);
		}
		initialTouchX = -1;
		initialTouchY = -1;
	} 
}