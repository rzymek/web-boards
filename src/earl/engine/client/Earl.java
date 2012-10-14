package earl.engine.client;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.vectomatic.dom.svg.OMSVGDocument;
import org.vectomatic.dom.svg.OMSVGElement;
import org.vectomatic.dom.svg.OMSVGImageElement;
import org.vectomatic.dom.svg.OMSVGPathElement;
import org.vectomatic.dom.svg.OMSVGStyle;
import org.vectomatic.dom.svg.impl.SVGDocument;
import org.vectomatic.dom.svg.impl.SVGPathElement;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style.OutlineStyle;
import com.google.gwt.dom.client.Style.Visibility;

public class Earl implements EntryPoint {
	public OMSVGImageElement selectedUnit = null;

	private final HexHandler hexHandler = new HexHandler(this);
	private final UnitHandler unitHandler= new UnitHandler(this);

	public void deselectUnit() {
		if (selectedUnit == null) {
			return;
		}
		OMSVGStyle style = selectedUnit.getStyle();
		style.setOutlineStyle(OutlineStyle.NONE);
		style.setOpacity(1);
		selectedUnit = null;
	}


	@Override
	public void onModuleLoad() {		
		final SVGDocument svg = getSVG();
		setStatus("Setting up hexes...");
		NodeList<Element> hexes = svg.getElementById("area").getElementsByTagName("path");
		for (int i = 0; i < hexes.getLength(); ++i) {
			SVGPathElement item = (SVGPathElement) hexes.getItem(i);
			OMSVGPathElement hex = OMSVGDocument.convert(item);
			hex.addMouseOverHandler(hexHandler);
			hex.addMouseOutHandler(hexHandler);
			hex.addClickHandler(hexHandler);
		}

		setStatus("Setting up units...");
		NodeList<Element> units = svg.getElementById("units").getElementsByTagName("image");
		for (int i = 0; i < units.getLength(); ++i) {
			Node node = units.getItem(i);
			if (node.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			Element element = (Element) node;
			if("image".equals(element.getTagName())) {
				OMSVGImageElement unit = OMSVGDocument.convert(node);
				unit.addClickHandler(unitHandler);
				unit.addMouseOverHandler(unitHandler);
				unit.addMouseOutHandler(unitHandler);
				
			}
		}
		setStatus("Restoring state...");
		EngineServiceAsync engine = GWT.create(EngineService.class);
		engine.getUnits(new EarlCallback<Map<String,String>>(){
			@Override
			public void onSuccess(Map<String, String> result) {
				Set<Entry<String, String>> units = result.entrySet();
				for (Entry<String, String> unit : units) {
					String unitId = unit.getKey();
					String hexId = unit.getValue();
					OMSVGImageElement svgunit = OMSVGDocument.convert(svg.getElementById(unitId));
					OMSVGPathElement svghex = OMSVGDocument.convert(svg.getElementById(hexId));
					hexHandler.moveToHex(svgunit, svghex);
				}
			}
		});
		OMSVGElement loadingLayer = OMSVGDocument.convert(svg.getElementById("loadingLayer"));
		loadingLayer.getStyle().setVisibility(Visibility.HIDDEN);
	}


	private void setStatus(String s) {
		log(s);
//		OMSVGElement status = OMSVGDocument.convert(svg.getElementById("loadingStatus"));
//		Iterator<OMNode> iterator = text.getChildNodes().iterator();
//		text.removeChild(iterator.next());
//		OMText node = text.getOwnerDocument().createTextNode(s);
//		text.appendChild(node);
	}

	public static native void log(Object s) /*-{
		if(console) {
			console.log(s);
		}
	}-*/;

	public static native SVGDocument getSVG() /*-{
		return $wnd.parent.view.document;
	}-*/;

}
