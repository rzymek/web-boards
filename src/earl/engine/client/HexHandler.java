package earl.engine.client;

import org.vectomatic.dom.svg.OMSVGDocument;
import org.vectomatic.dom.svg.OMSVGElement;
import org.vectomatic.dom.svg.OMSVGImageElement;
import org.vectomatic.dom.svg.OMSVGMatrix;
import org.vectomatic.dom.svg.OMSVGPathElement;
import org.vectomatic.dom.svg.OMSVGPoint;
import org.vectomatic.dom.svg.OMSVGRect;
import org.vectomatic.dom.svg.OMSVGSVGElement;
import org.vectomatic.dom.svg.OMSVGStyle;
import org.vectomatic.dom.svg.impl.SVGPathElement;

import com.google.gwt.dom.client.Style.OutlineStyle;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;

public class HexHandler implements MouseOutHandler, MouseOverHandler, ClickHandler {

	private OMSVGImageElement selectedUnit = null;

	private void deselectUnit() {
		if (selectedUnit == null) {
			return;
		}
		OMSVGStyle style = selectedUnit.getStyle();
		style.setOutlineStyle(OutlineStyle.NONE);
		style.setOpacity(1);
		selectedUnit = null;
	}

	private OMSVGPoint getCenter(OMSVGPathElement e) {
		OMSVGRect bbox = e.getBBox();
		OMSVGSVGElement svg = e.getOwnerSVGElement();
		OMSVGPoint p = svg.createSVGPoint();
		float x = bbox.getX() + bbox.getWidth() / 2.0f;
		float y = bbox.getY() + bbox.getHeight() / 2.0f;
		p.setX(x);
		p.setY(y);
		return p;
	}

	private void moveToHex(OMSVGImageElement unit, OMSVGPathElement hex) {
		OMSVGRect bbox = unit.getBBox();
		OMSVGMatrix m = hex.getTransformToElement(unit);
		OMSVGPoint to = getCenter(hex);
		to = to.matrixTransform(m);

		float x = to.getX() - bbox.getWidth() / 2.0f;
		float y = to.getY() + bbox.getHeight() / 2.0f;
		unit.getX().getBaseVal().setValue(x);
		unit.getY().getBaseVal().setValue(y);
	}

	@Override
	public void onClick(ClickEvent event) {
		if (selectedUnit == null) {
			return;
		}
		OMSVGPathElement hex = getHex(event);
		// drawMove(selectedUnit, hex);
		moveToHex(selectedUnit, hex);
		// parent.menu.gwtexpEarl(selectedUnit.id, e.target.id);
		deselectUnit();
	}

	protected OMSVGPathElement getHex(DomEvent<?> event) {
		SVGPathElement relativeElement = (SVGPathElement) event.getRelativeElement();
		return OMSVGDocument.convert(relativeElement);
	}

	@Override
	public void onMouseOver(MouseOverEvent evt) {
		setTargetOpacity(evt, 1);
	}

	@Override
	public void onMouseOut(MouseOutEvent evt) {
		setTargetOpacity(evt, 0.33);
	}

	protected void setTargetOpacity(DomEvent<?> evt, double opacity) {
		OMSVGElement hex = getHex(evt);
		hex.getStyle().setOpacity(opacity);
	}
}
