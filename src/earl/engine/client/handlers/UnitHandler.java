package earl.engine.client.handlers;

import org.vectomatic.dom.svg.OMSVGDocument;
import org.vectomatic.dom.svg.OMSVGImageElement;
import org.vectomatic.dom.svg.OMSVGStyle;

import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.OutlineStyle;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;

import earl.engine.client.Earl;

public class UnitHandler implements ClickHandler, MouseOverHandler, MouseOutHandler {

	private final Earl earl;

	public UnitHandler(Earl earl) {
		this.earl = earl;
	}

	@Override
	public void onMouseOut(MouseOutEvent event) {
	}

	@Override
	public void onMouseOver(MouseOverEvent event) {
	}

	@Override
	public void onClick(ClickEvent event) {
		OMSVGImageElement last = earl.selectedUnit;
		earl.deselectUnit();
		OMSVGImageElement unit = getUnit(event);
		if (last == unit) {
			flip(last);
			return;
		}
		earl.selectedUnit = unit;
		OMSVGStyle style = earl.selectedUnit.getStyle();
		style.setOutlineColor("rgba(255,0,0,0.5)");
		style.setOutlineStyle(OutlineStyle.SOLID);
		style.setOutlineWidth(5.0, Unit.PX);
		style.setOpacity(0.8);
		style.setBorderStyle(BorderStyle.SOLID);
		style.setBorderWidth(3.0, Unit.PX);
		style.setBorderColor("green");
		unit.getParentNode().appendChild(unit);// move on top
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
		String back = earl.getGameInfo().sides.get(name);
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
