package webboards.client.display.svg;

import org.vectomatic.dom.svg.impl.SVGImageElement;
import org.vectomatic.dom.svg.impl.SVGSVGElement;

import webboards.client.Resources;

import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;

public class SVGLowResZoomPan extends SVGZoomAndPanHandler {
	private boolean lowRes = false;

	public SVGLowResZoomPan(SVGSVGElement svg) {
		super(svg);
	}

	private void updateImageResolution() {
		SVGImageElement boardImg = (SVGImageElement) svg.getElementById("img");
		boardImg.getHref().setBaseVal(lowRes ? 
			Resources.INSTANCE.boardLow().getSafeUri().asString(): 
			Resources.INSTANCE.board().getSafeUri().asString());
	}
	
	@Override
	public void onMouseMove(MouseMoveEvent e) {
		if (mouseDown) {
			if (!lowRes) {
				lowRes = true;
				updateImageResolution();
			}		
		}
		super.onMouseMove(e);
	}
	
	@Override
	public void onMouseUp(MouseUpEvent event) {
		super.onMouseUp(event);
		lowRes = false;
		updateImageResolution();
	}

}
