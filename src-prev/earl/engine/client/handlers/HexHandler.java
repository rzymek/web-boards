package earl.engine.client.handlers;

import org.vectomatic.dom.svg.OMElement;
import org.vectomatic.dom.svg.OMSVGDocument;
import org.vectomatic.dom.svg.OMSVGElement;
import org.vectomatic.dom.svg.OMSVGImageElement;
import org.vectomatic.dom.svg.OMSVGMatrix;
import org.vectomatic.dom.svg.OMSVGPoint;
import org.vectomatic.dom.svg.OMSVGRect;
import org.vectomatic.dom.svg.OMSVGSVGElement;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;

import earl.engine.client.Earl;
import earl.engine.client.EngineService;
import earl.engine.client.EngineServiceAsync;
import earl.engine.client.SVGDisplay;
import earl.engine.client.utils.EarlCallback;
import earl.engine.client.utils.SVGUtils;

public class HexHandler implements MouseOutHandler, MouseOverHandler, ClickHandler {

	private final SVGDisplay display;

	public HexHandler(SVGDisplay display) {
		this.display = display;
	}

	public static OMSVGPoint getCenter(OMSVGElement e) {
		OMSVGRect bbox = SVGUtils.getBBox(e);
		OMSVGSVGElement svg = e.getOwnerSVGElement();
		OMSVGPoint p = svg.createSVGPoint();
		float x = bbox.getX() + bbox.getWidth() / 2.0f;
		float y = bbox.getY() + bbox.getHeight() / 2.0f;
		p.setX(x);
		p.setY(y);
		return p;
	}

	public void moveToHex(OMSVGImageElement unit, OMSVGElement hex) {
		String from = unit.getAttribute("earlPosition");
		if(from != null && !from.isEmpty()) {
			OMSVGSVGElement svg = unit.getOwnerSVGElement();
			OMElement area = svg.getElementById(from);
			String unitList = area.getAttribute("earlUnits");
			String[] units = unitList.split(":");
			unitList = "";
			for (String unitId : units) {
				if(unit.getId().equals(unitId)) {
					continue;
				}
				if(!unitList.isEmpty()) unitList+=":";
				unitList += unitId;
			}
			area.setAttribute("earlUnits", unitList);
		}
		unit.setAttribute("earlPosition", hex.getId());
		String unitList = hex.getAttribute("earlUnits");
		if (unitList == null || unitList.isEmpty()) {
			unitList = unit.getId();
		} else {
			unitList += ":" + unit.getId();
		}
		hex.setAttribute("earlUnits", unitList);
		
		alignStack(hex);
	}

	public static void alignStack(OMSVGElement hex) {
		String unitList = hex.getAttribute("earlUnits");
		String[] stackIds = unitList.split(":");
		OMSVGSVGElement svg = hex.getOwnerSVGElement();
		int offset = 0;
		for (String unitId : stackIds) {
			OMSVGImageElement unit = (OMSVGImageElement) svg.getElementById(unitId);
			OMSVGRect bbox = SVGUtils.getBBox(unit);
			OMSVGMatrix m = SVGUtils.getTransformToElement(hex, unit);
			OMSVGPoint to = getCenter(hex);
			to = to.matrixTransform(m);

			float x = to.getX() - bbox.getWidth() / 2.0f + offset;
			float y = to.getY() - bbox.getHeight() / 2.0f + offset;
			unit.getX().getBaseVal().setValue(x);
			unit.getY().getBaseVal().setValue(y);
			
			offset += 3;
		}
	}

	@Override
	public void onClick(ClickEvent event) {
		OMSVGImageElement selectedPiece = display.getSelectedPiece();
		if (selectedPiece == null) {
			return;
		}
		OMSVGElement hex = getHex(event);
		// drawMove(earl.selectedUnit, hex);
		moveToHex(selectedPiece, hex);
		Earl.log(selectedPiece.getId() + " ->" + hex.getId());
		// parent.menu.gwtexpEarl(earl.selectedUnit.id, e.target.id);
		EngineServiceAsync engine = GWT.create(EngineService.class);
		engine.updateLocation(selectedPiece.getId(), hex.getId(), new EarlCallback<Void>());
		display.setSelectedPiece(null);
	}

	protected OMSVGElement getHex(DomEvent<?> event) {
		return OMSVGDocument.convert(event.getRelativeElement());
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
		OMSVGElement hex = OMSVGDocument.convert(evt.getRelativeElement());
		hex.getStyle().setOpacity(opacity);
	}
}
