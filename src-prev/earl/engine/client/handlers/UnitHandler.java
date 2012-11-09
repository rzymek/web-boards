package earl.engine.client.handlers;

import org.vectomatic.dom.svg.OMSVGDocument;
import org.vectomatic.dom.svg.OMSVGElement;
import org.vectomatic.dom.svg.OMSVGGElement;
import org.vectomatic.dom.svg.OMSVGImageElement;
import org.vectomatic.dom.svg.OMSVGMatrix;
import org.vectomatic.dom.svg.OMSVGPoint;
import org.vectomatic.dom.svg.OMSVGRect;
import org.vectomatic.dom.svg.OMSVGRectElement;
import org.vectomatic.dom.svg.OMSVGSVGElement;
import org.vectomatic.dom.svg.OMSVGStyle;

import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.OutlineStyle;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import earl.engine.client.Earl;
import earl.engine.client.SVGDisplay;
import earl.engine.client.utils.SVGUtils;

public class UnitHandler implements ClickHandler {

	private final SVGDisplay display;
	private OMSVGElement currentStack = null;
	private final OMSVGRectElement stackRect;

	public UnitHandler(SVGDisplay display) {
		this.display = display;
		stackRect = new OMSVGRectElement();
		stackRect.setAttribute("style","fill:#d5d5d5;fill-opacity:1;fill-rule:evenodd;stroke:#000000;stroke-width:1;stroke-opacity:1");
	}

	@Override
	public void onClick(ClickEvent event) {
		if(currentStack != null) {
			HexHandler.alignStack(currentStack);
			
			OMSVGGElement g = OMSVGDocument.convert(display.getSVG().getElementById("units"));
			g.removeChild(stackRect);
			currentStack = null;
			return;
		}
		OMSVGImageElement last = display.getSelectedPiece();
		display.setSelectedPiece(null);
		OMSVGImageElement unit = getUnit(event);
		if (last == unit) {
			flip(last);
			return;
		}
		if(!showStack(unit)) {
			OMSVGStyle style = display.getSelectedPiece().getStyle();
			style.setOutlineColor("rgba(255,0,0,0.5)");
			style.setOutlineStyle(OutlineStyle.SOLID);
			style.setOutlineWidth(5.0, Unit.PX);
			style.setOpacity(0.8);
			style.setBorderStyle(BorderStyle.SOLID);
			style.setBorderWidth(3.0, Unit.PX);
			style.setBorderColor("green");
			unit.getParentNode().appendChild(unit);// move on top
		}
	}

	protected boolean showStack(OMSVGImageElement unit) {
		String hexId = unit.getAttribute("earlPosition");
		if(hexId == null || hexId.isEmpty()) {
			return false;
		}
		OMSVGSVGElement svg = unit.getOwnerSVGElement();
		OMSVGElement position = (OMSVGElement) svg.getElementById(hexId);
		String earlUnits = position.getAttribute("earlUnits");
		String[] unitIds = earlUnits.split(":");
		if(unitIds.length > 1) {
			OMSVGMatrix m = SVGUtils.getTransformToElement(position, unit);
			OMSVGPoint offset = HexHandler.getCenter(position);
			offset = offset.matrixTransform(m);
			float rx=0;
			float ry=0;
			float width = 0;
			float height = 0;
			unit.getParentNode().appendChild(stackRect);
			for (String unitId : unitIds) {
				OMSVGImageElement stacked = (OMSVGImageElement) svg.getElementById(unitId);
				OMSVGRect bbox = SVGUtils.getBBox(stacked);
				float x = offset.getX() - bbox.getWidth() / 2.0f;
				float y = offset.getY() - bbox.getHeight() / 2.0f;
				if(rx == 0) {
					rx = x;
					ry = y;
				}
				stacked.getX().getBaseVal().setValue(x);
				stacked.getY().getBaseVal().setValue(y);
				offset.setX(offset.getX() + bbox.getWidth() + 3);
				unit.getParentNode().appendChild(stacked);
				width += bbox.getWidth() + 3;
				height = bbox.getHeight() + 8;
			}
			stackRect.getX().getBaseVal().setValue(rx-5);
			stackRect.getY().getBaseVal().setValue(ry-5);
			stackRect.getWidth().getBaseVal().setValue(width+10);
			stackRect.getHeight().getBaseVal().setValue(height);
			currentStack = position;			
			return true;
		}else{
			return false;
		}
	}

	private void flip(OMSVGImageElement unit) {
		Earl.log(unit.getId()+" flipped.");
		String src = unit.getHref().getBaseVal();
		String earlFlipped = unit.getAttribute("earlFlipped");
		unit.setAttribute("earlFlipped", src);
		if("".equals(earlFlipped) || earlFlipped==null) {
			src = flip(src);
		}else{
			src = earlFlipped;
		}
		unit.getHref().setBaseVal(src);
	}

	protected String flip(String src) {
		String name = src.substring("units/".length());
		String back = display.getGameInfo().sides.get(name);
		if(back != null) {
			return "units/"+back;
		}else{
			return src;
		}
//		if (src.endsWith("_f.png")) {
//			String dir = src.substring(0, src.lastIndexOf('/') + 1);
//			src = src.substring(src.lastIndexOf('+') + 1);
//			src = src.replace("_f.png", "_b.png");
//			dir = dir.replace("/front/", "/back/");
//			src = dir + src;
//		}
//		return src;
	}

	private OMSVGImageElement getUnit(ClickEvent event) {
		return OMSVGDocument.convert(event.getRelativeElement());
	}

}
