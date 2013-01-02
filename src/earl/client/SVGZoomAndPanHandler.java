package earl.client;

import org.vectomatic.dom.svg.OMSVGRect;
import org.vectomatic.dom.svg.impl.SVGImageElement;
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

	private static final float MIN_SCALE = 0.05f;
	private Position start = null;
	private Position size = null;
	private final SVGSVGElement svg;
	private float scale = 1.0f;
	private boolean lowRes = false;

	public SVGZoomAndPanHandler(SVGSVGElement svg) {
		this.svg = svg;
		OMSVGRect viewbox = svg.getViewBox().getBaseVal();
		size = new Position((int)viewbox.getWidth(), (int)viewbox.getHeight());
	}

	@Override
	public void onMouseWheel(MouseWheelEvent event) {
		float s1 = scale;
		scale -= event.getDeltaY() / 20.0f;
		if (scale < MIN_SCALE)
			scale = MIN_SCALE;
//		float x = event.getRelativeX(svg);
//		float y = event.getRelativeY(svg);
		float x = event.getX();
		float y = event.getY();
		ClientEngine.log(x + ", " + y);
		OMSVGRect viewbox = svg.getViewBox().getBaseVal();
		float dx = x * (s1 - scale);
		float dy = y * (s1 - scale);
		viewbox.setX(viewbox.getX() - dx);		
		viewbox.setY(viewbox.getY() - dy);
		viewbox.setWidth(size.x / scale);
		viewbox.setHeight(size.y / scale);		
		event.preventDefault();
	}

	@Override
	public void onMouseMove(MouseMoveEvent e) {
		int x = e.getX();
		int y = e.getY();
		
		if (start == null) {
			return;
		}
		if(!lowRes) {
			SVGImageElement boardImg = (SVGImageElement) svg.getElementById("img");
			boardImg.getHref().setBaseVal("board-low.jpg");
			lowRes = true;
		}
		OMSVGRect viewBox = svg.getViewBox().getBaseVal();
		viewBox.setX(viewBox.getX() + (start.x - x)/scale);
		viewBox.setY(viewBox.getY() + (start.y - y)/scale);
//		Document.get().getElementById("menu").setInnerHTML(x+","+y+"<br>"+
//				e.getClientX()+","+e.getClientY()+"<br>"+
//				e.getScreenX()+","+e.getScreenY()+"<br>"+
//				e.getRelativeX(svg)+","+e.getRelativeY(svg)+"<br>"+
//				scale+"<br>"+
//				viewBox.getX()+","+viewBox.getY()
//		);
		start.x = x;
		start.y = y;
		e.preventDefault();        
	}

	@Override
	public void onMouseUp(MouseUpEvent event) {
//		ClientEngine.log("mouse up");
		start = null;
		SVGImageElement boardImg = (SVGImageElement) svg.getElementById("img");
		boardImg.getHref().setBaseVal("board.jpg");
		lowRes = false;
	}

	@Override
	public void onMouseDown(MouseDownEvent event) {
		start = new Position(event.getX(), event.getY());
	}

}
