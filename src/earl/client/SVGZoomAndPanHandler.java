package earl.client;

import org.vectomatic.dom.svg.OMSVGRect;
import org.vectomatic.dom.svg.impl.SVGSVGElement;

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

	public SVGZoomAndPanHandler(SVGSVGElement svg) {
		this.svg = svg;
	}

	@Override
	public void onMouseWheel(MouseWheelEvent event) {
		float deltaY = 1.0f + event.getDeltaY() / 20.0f;
		OMSVGRect viewbox = svg.getViewBox().getBaseVal();
		viewbox.setWidth(viewbox.getWidth() * deltaY);
		viewbox.setHeight(viewbox.getHeight() * deltaY);
		int x = event.getX();
		int y = event.getY();
		ClientEngine.log(x+", "+y);		
		event.preventDefault();
	}

	@Override
	public void onMouseMove(MouseMoveEvent event) {
		if (start == null) {
			return;
		}
		OMSVGRect viewBox = svg.getViewBox().getBaseVal();
		int x = event.getX();
		int y = event.getY();

		viewBox.setX(viewBox.getX() + (start.x - x));
		viewBox.setY(viewBox.getY() + (start.y - y));
		start.x = x;
		start.y = y;
	}

	@Override
	public void onMouseUp(MouseUpEvent event) {
		ClientEngine.log("mouse up");
		start = null;
	}

	@Override
	public void onMouseDown(MouseDownEvent event) {
		start = new Position(event.getX(), event.getY());
	}

}
