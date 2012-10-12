package earl.engine.client;

import org.vectomatic.dom.svg.OMSVGDocument;
import org.vectomatic.dom.svg.OMSVGImageElement;
import org.vectomatic.dom.svg.OMSVGPathElement;
import org.vectomatic.dom.svg.OMSVGStyle;
import org.vectomatic.dom.svg.impl.SVGDocument;
import org.vectomatic.dom.svg.impl.SVGPathElement;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style.OutlineStyle;

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
		log("Earl - Starting....");
		SVGDocument svg = getSVG();
		NodeList<Element> hexes = svg.getElementById("area").getElementsByTagName("path");
		for (int i = 0; i < hexes.getLength(); ++i) {
			SVGPathElement item = (SVGPathElement) hexes.getItem(i);
			OMSVGPathElement hex = OMSVGDocument.convert(item);
			hex.addMouseOverHandler(hexHandler);
			hex.addMouseOutHandler(hexHandler);
			hex.addClickHandler(hexHandler);
		}

		NodeList<Node> units = svg.getElementById("units").getChildNodes();
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

		log("Earl - Started");
	}

	public static native void log(Object s) /*-{
		console.log(s);
	}-*/;

	public static native SVGDocument getSVG() /*-{
		return $wnd.parent.view.document;
	}-*/;

}
