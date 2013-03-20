package webboards.client.display.svg;

import org.vectomatic.dom.svg.OMDocument;
import org.vectomatic.dom.svg.OMSVGMatrix;
import org.vectomatic.dom.svg.OMSVGPoint;
import org.vectomatic.dom.svg.OMSVGRect;
import org.vectomatic.dom.svg.OMSVGSVGElement;
import org.vectomatic.dom.svg.impl.SVGImageElement;
import org.vectomatic.dom.svg.impl.SVGSVGElement;

import webboards.client.Resources;
import webboards.client.display.VisualCoords;
import webboards.client.utils.Browser;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
import com.google.gwt.user.client.ui.RootPanel;

public class SVGZoomAndPanHandler implements MouseDownHandler, MouseUpHandler, MouseMoveHandler, MouseWheelHandler, KeyPressHandler, ClickHandler {
	private static final float KEY_ZOOM_STEP = 1.3f;
	private static boolean LOW_RES_PANNING = true;
	private float minScale = 0.25f;
	private final VisualCoords size;
	private final VisualCoords mouse = new VisualCoords(0, 0);
	private final VisualCoords offset = new VisualCoords(0, 0);	
	private final SVGSVGElement svg;
	private float scale = 1.0f;
	private boolean lowRes = false;
	private boolean mouseDown = false;
	private static boolean panning = false;

	private SVGZoomAndPanHandler(SVGSVGElement svg) {
		this.svg = svg;
		OMSVGRect viewbox = svg.getViewBox().getBaseVal();
		size = new VisualCoords((int) viewbox.getWidth(), (int) viewbox.getHeight());
		minScale = Math.min(Window.getClientWidth()  / viewbox.getWidth(), Window.getClientHeight() / viewbox.getHeight());
		LOW_RES_PANNING = (Window.Location.getParameter("low")!=null);
		Browser.console("LOW_RES_PANNING=["+Window.Location.getParameter("low")+"]");
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
		if (mouseDown) {
			panning = true;
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
			panning = false;
			updateMousePosition(e);
		}
	}

	private native static void stop(NativeEvent e) /*-{
		if(e.stopPropagation) e.stopPropagation();
		if(e.preventDefault)e.preventDefault();
		if(e.stopImmediatePropagation)e.stopImmediatePropagation();
		console.log("stopped "+e);
	}-*/;

	private void updateImageResolution() {
		if(LOW_RES_PANNING) {
			SVGImageElement boardImg = (SVGImageElement) svg.getElementById("img");
			boardImg.getHref().setBaseVal(lowRes ? 
				Resources.INSTANCE.boardLow().getSafeUri().asString(): 
				Resources.INSTANCE.board().getSafeUri().asString());
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
		mouseDown = false;
		lowRes = false;
		updateImageResolution();
	}

	@Override
	public void onMouseDown(MouseDownEvent event) {
		updateMousePosition(event);
		mouseDown = true;
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

	public static void attach(SVGSVGElement svg) {
		SVGZoomAndPanHandler zoomAndPan = new SVGZoomAndPanHandler(svg);
		OMSVGSVGElement omsvg = OMDocument.convert(svg);
		omsvg.addMouseDownHandler(zoomAndPan);
		omsvg.addMouseUpHandler(zoomAndPan);
		omsvg.addMouseMoveHandler(zoomAndPan);
		omsvg.addClickHandler(zoomAndPan);
		RootPanel.get().addDomHandler(zoomAndPan, MouseWheelEvent.getType());
		RootPanel.get().addDomHandler(zoomAndPan, KeyPressEvent.getType());		
	}

	@Override
	public void onClick(ClickEvent event) {
		panning = false;
	}

	public static boolean isPanning() {
		return panning;
	}
}
