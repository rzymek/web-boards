package earl.client.dom;

import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.core.client.JavaScriptObject;

public class SVGRect extends JavaScriptObject {
	protected SVGRect() {
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

	public final native float getWidth() /*-{
		return this.width;
	}-*/;

	public final native void setWidth(float value) throws JavaScriptException /*-{
		this.width = value;
	}-*/;

	public final native float getHeight() /*-{
		return this.height;
	}-*/;

	public final native void setHeight(float value) throws JavaScriptException /*-{
		this.height = value;
	}-*/;

	public final native SVGPoint getCenter(SVGElement svg) /*-{
		return svg.createSVGPoint(this.x + 0.5 * this.width,
				this.y + 0.5 * this.height);
	}-*/;
}
