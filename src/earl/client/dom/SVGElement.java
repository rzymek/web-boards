package earl.client.dom;

import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.user.client.Element;

public class SVGElement extends Element {
	protected SVGElement() {
	}

	public final native SVGRect getBBox() /*-{
		console.log("bbox:"+this);
		return this.getBBox();
	}-*/;

	public final native SVGMatrix getTransformToElement(SVGElement element) throws JavaScriptException /*-{
		console.log("svglelement.getTransformToElement");
		console.log(this);
		console.log(this.getTransformToElement);
		return this.getTransformToElement(element);
	}-*/;

	public final SVGPoint getCenter() {
		return getBBox().getCenter(getOwnerSVGElement());
	}

	public final native float getX() /*-{
		return this.x.baseVal.value;
	}-*/;

	public final native float getY() /*-{
		return this.y.baseVal.value;
	}-*/;

	public final native void setX(float x) /*-{
		this.x.baseVal.value = x;
	}-*/;

	public final native void setY(float y) /*-{
		this.x.baseVal.value = y;
	}-*/;
	public final native SVGElement getOwnerSVGElement() /*-{
		return this.ownerSVGElement;
	}-*/;
}
