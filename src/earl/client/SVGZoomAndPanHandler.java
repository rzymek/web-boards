package earl.client;

import org.vectomatic.dom.svg.OMSVGRect;
import org.vectomatic.dom.svg.impl.SVGSVGElement;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;

import earl.client.op.Position;

public class SVGZoomAndPanHandler implements MouseDownHandler, MouseUpHandler, MouseMoveHandler, MouseWheelHandler {

	private Position start = null;
	private final SVGSVGElement svg;
	private float scale = 1.0f;

	public SVGZoomAndPanHandler(SVGSVGElement svg) {
		this.svg = svg;
	}

	@Override
	public void onMouseWheel(MouseWheelEvent event) {
		scale += event.getDeltaY() / 20.0f;
		float x = event.getX();
		float y = event.getY();
		ClientEngine.log(x + ", " + y);
		OMSVGRect viewbox = svg.getViewBox().getBaseVal();
		viewbox.setWidth(viewbox.getWidth() * scale);
		viewbox.setHeight(viewbox.getHeight() * scale);
		x -= viewbox.getX();
		y -= viewbox.getY();
		viewbox.setX(viewbox.getX() / scale);
		viewbox.setY(viewbox.getY() / scale);
		event.preventDefault();
	}

	@Override
	public void onMouseMove(MouseMoveEvent e) {
		int x = e.getX();
		int y = e.getY();
		
		Document.get().getElementById("menu").setInnerHTML(x+","+y+"<br>"+
				e.getClientX()+","+e.getClientY()+"<br>"+
				e.getScreenX()+","+e.getScreenY()+"<br>"+
				e.getRelativeX(svg)+","+e.getRelativeY(svg));
		if (start == null) {
			return;
		}
		e.preventDefault();
		OMSVGRect viewBox = svg.getViewBox().getBaseVal();
		viewBox.setX(viewBox.getX() + (start.x - x));
		viewBox.setY(viewBox.getY() + (start.y - y));
		start.x = x;
		start.y = y;
	}

	@Override
	public void onMouseUp(MouseUpEvent event) {
//		ClientEngine.log("mouse up");
		start = null;
	}

	@Override
	public void onMouseDown(MouseDownEvent event) {
		start = new Position(event.getX(), event.getY());
	}

}
