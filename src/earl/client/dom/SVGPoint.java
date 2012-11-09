package earl.client.dom;

import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.core.client.JavaScriptObject;

public class SVGPoint extends JavaScriptObject {
	protected SVGPoint() {
	}

	public final native float getX() /*-{
		return this.x;
	}-*/;

	public final native void setX(float value) throws JavaScriptException /*-{
		this.x = value;
	}-*/;

	public final native float getY() /*-{
		return this.y;
	}-*/;

	public final native void setY(float value) throws JavaScriptException /*-{
		this.y = value;
	}-*/;

	public final native SVGPoint matrixTransform(SVGMatrix matrix) /*-{
		return this.matrixTransform(matrix);
	}-*/;
}
