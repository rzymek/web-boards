package webboards.client.display.svg;

import org.vectomatic.dom.svg.OMSVGMatrix;
import org.vectomatic.dom.svg.OMSVGPoint;
import org.vectomatic.dom.svg.OMSVGRect;
import org.vectomatic.dom.svg.impl.SVGImageElement;
import org.vectomatic.dom.svg.impl.SVGSVGElement;

import webboards.client.ClientEngine;
import webboards.client.display.VisualCoords;

import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.user.client.Window;

public class SVGZoomAndPanHandler implements MouseDownHandler, MouseUpHandler, MouseMoveHandler, MouseWheelHandler, KeyPressHandler {
	private static final float KEY_ZOOM_STEP = 1.3f;
	private static final boolean LOW_RES_PANNING = false;
	private float minScale = 0.25f;
	private final VisualCoords size;
	private final VisualCoords mouse = new VisualCoords(0, 0);
	private final VisualCoords offset = new VisualCoords(0, 0);	
	private final SVGSVGElement svg;
	private float scale = 1.0f;
	private boolean lowRes = false;
	private boolean panning = false;

	public SVGZoomAndPanHandler(SVGSVGElement svg) {
		this.svg = svg;
		OMSVGRect viewbox = svg.getViewBox().getBaseVal();
		size = new VisualCoords((int) viewbox.getWidth(), (int) viewbox.getHeight());
		minScale = Math.min(Window.getClientWidth()  / viewbox.getWidth(), Window.getClientHeight() / viewbox.getHeight());
		ClientEngine.log("minScale="+minScale);
	}

	@Override
	public void onMouseWheel(MouseWheelEvent event) {
		if(event.getDeltaY() > 0) {
			scale /= KEY_ZOOM_STEP;
		} else {
			scale *= KEY_ZOOM_STEP;
		}
		updateZoom();
		event.preventDefault();
	}

	private void updateZoom() {
		if (scale < minScale)
			scale = minScale;
		float x = mouse.x;
		float y = mouse.y;
		OMSVGPoint before = toUsertSpace(x, y);
		OMSVGRect viewbox = svg.getViewBox().getBaseVal();
		viewbox.setWidth(size.x / scale);
		viewbox.setHeight(size.y / scale);
		OMSVGPoint after = toUsertSpace(x, y);
		float dx = before.getX() - after.getX();
		float dy = before.getY() - after.getY();
		viewbox.setX(viewbox.getX() + dx);
		viewbox.setY(viewbox.getY() + dy);
	}

	private OMSVGPoint toUsertSpace(float x, float y) {
		OMSVGMatrix ctm = svg.getScreenCTM();
		OMSVGPoint p = svg.createSVGPoint();
		p.setX(x);
		p.setY(y);
		p = p.matrixTransform(ctm.inverse());
		return p;
	}

	@Override
	public void onMouseMove(MouseMoveEvent e) {
		if (panning) {
			e.preventDefault();
			float x = mouse.x;
			float y = mouse.y;
			OMSVGPoint start = toUsertSpace(x, y);
			OMSVGPoint pos = toUsertSpace(e.getClientX(), e.getClientY());
			if (!lowRes) {
				lowRes = true;
				updateImageResolution();
			}
			OMSVGRect viewBox = svg.getViewBox().getBaseVal();
			viewBox.setX(offset.x + (start.getX() - pos.getX()));
			viewBox.setY(offset.y + (start.getY() - pos.getY()));
		} else {
			updateMousePosition(e);
		}
	}

	private void updateImageResolution() {
		if(LOW_RES_PANNING) {
			SVGImageElement boardImg = (SVGImageElement) svg.getElementById("img");
			boardImg.getHref().setBaseVal(lowRes ? "board-low.jpg" : "board.jpg");
		}
	}

	private void updateMousePosition(MouseEvent<?> e) {
		mouse.x = e.getClientX();
		mouse.y = e.getClientY();
		OMSVGRect viewbox = svg.getViewBox().getBaseVal();
		offset.x = (int) viewbox.getX();
		offset.y = (int) viewbox.getY();
	}

	@Override
	public void onMouseUp(MouseUpEvent event) {
		if (panning) {
			event.preventDefault();
		}
		panning = false;
		lowRes = false;
		updateImageResolution();
	}

	@Override
	public void onMouseDown(MouseDownEvent event) {
		updateMousePosition(event);
		panning = true;
	}

	@Override
	public void onKeyPress(KeyPressEvent event) {
		char c = event.getCharCode();
		switch (c) {
		case '[':case 'q':
			scale /= KEY_ZOOM_STEP;
			updateZoom();
			break;
		case ']':case 'w':
			scale *= KEY_ZOOM_STEP;
			updateZoom();
			break;
		case '\\': case '1':
			scale = 1;
			updateZoom();
			OMSVGRect viewBox = svg.getViewBox().getBaseVal();
			viewBox.setX(0);
			viewBox.setY(0);
			break;
		}
	}

}
