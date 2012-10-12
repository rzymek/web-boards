package earl.engine.client;

import org.vectomatic.dom.svg.OMSVGDocument;
import org.vectomatic.dom.svg.OMSVGPathElement;
import org.vectomatic.dom.svg.impl.SVGDocument;
import org.vectomatic.dom.svg.impl.SVGPathElement;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverHandler;

public class Earl implements EntryPoint {

	private final MouseOutHandler onMouseOutHex;
	private final MouseOverHandler onMouseOverHex;
	private final ClickHandler hexClicked;

	public Earl() {
		HexHandler hexHandler = new HexHandler();
		onMouseOutHex = hexHandler;
		onMouseOverHex = hexHandler;
		hexClicked = hexHandler;		
	}
	
	@Override
	public void onModuleLoad() {
		log("Earl - Starting....");		
		SVGDocument svg = getSVG();
		NodeList<Element> hexes = svg.getElementById("area").getElementsByTagName("path");
		for (int i=0;i<hexes.getLength();++i) {
			OMSVGPathElement hex = OMSVGDocument.convert((SVGPathElement) hexes.getItem(i));
			hex.addMouseOverHandler(onMouseOverHex);
			hex.addMouseOutHandler(onMouseOutHex);
			hex.addClickHandler(hexClicked);
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
