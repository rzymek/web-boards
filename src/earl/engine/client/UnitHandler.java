package earl.engine.client;

import org.vectomatic.dom.svg.OMSVGDocument;
import org.vectomatic.dom.svg.OMSVGImageElement;
import org.vectomatic.dom.svg.OMSVGStyle;

import com.google.gwt.dom.client.Style.OutlineStyle;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;

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
			return;
		}
		earl.selectedUnit = unit;
		OMSVGStyle style = earl.selectedUnit.getStyle();
		style.setOutlineColor("rgba(255,0,0,0.5)");
		style.setOutlineStyle(OutlineStyle.SOLID);
		style.setOutlineWidth(10.0, Unit.PX);
		style.setOpacity(0.8);
		unit.getParentNode().appendChild(unit);//move on top
	}

	private OMSVGImageElement getUnit(ClickEvent event) {
		return OMSVGDocument.convert(event.getRelativeElement());
	}

}
