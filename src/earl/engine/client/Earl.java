package earl.engine.client;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.vectomatic.dom.svg.OMSVGDocument;
import org.vectomatic.dom.svg.OMSVGElement;
import org.vectomatic.dom.svg.OMSVGGElement;
import org.vectomatic.dom.svg.OMSVGImageElement;
import org.vectomatic.dom.svg.OMSVGPathElement;
import org.vectomatic.dom.svg.OMSVGStyle;
import org.vectomatic.dom.svg.OMSVGTSpanElement;
import org.vectomatic.dom.svg.OMSVGTextElement;
import org.vectomatic.dom.svg.impl.SVGPathElement;
import org.vectomatic.dom.svg.impl.SVGSVGElement;

import com.google.gwt.appengine.channel.client.Channel;
import com.google.gwt.appengine.channel.client.ChannelFactory;
import com.google.gwt.appengine.channel.client.ChannelFactory.ChannelCreatedCallback;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.OutlineStyle;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;

public class Earl implements EntryPoint {
	public OMSVGImageElement selectedUnit = null;
	public SVGSVGElement svg;
	public final HexHandler hexHandler = new HexHandler(this);
	public final UnitHandler unitHandler = new UnitHandler(this);

	public void deselectUnit() {
		if (selectedUnit == null) {
			return;
		}
		OMSVGStyle style = selectedUnit.getStyle();
		style.setOutlineStyle(OutlineStyle.NONE);
		style.setBorderStyle(BorderStyle.NONE);
		style.setOpacity(1);
		selectedUnit = null;
	}

	
	@Override
	public void onModuleLoad() {
		init();
	}
		
	public void init() {
		svg = getSVG();
		log("Setting up hexes...");
		NodeList<Element> hexes = svg.getElementById("area").getElementsByTagName("path");
		for (int i = 0; i < hexes.getLength(); ++i) {
			SVGPathElement item = (SVGPathElement) hexes.getItem(i);
			OMSVGPathElement hex = OMSVGDocument.convert(item);
			hex.addMouseOverHandler(hexHandler);
			hex.addMouseOutHandler(hexHandler);
			hex.addClickHandler(hexHandler);
		}

		log("Setting up units...");
		NodeList<Element> units = svg.getElementById("units").getElementsByTagName("image");
		for (int i = 0; i < units.getLength(); ++i) {
			Node node = units.getItem(i);
			if (node.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			Element element = (Element) node;
			if ("image".equals(element.getTagName())) {
				OMSVGImageElement unit = OMSVGDocument.convert(node);
				unit.addClickHandler(unitHandler);
				unit.addMouseOverHandler(unitHandler);
				unit.addMouseOutHandler(unitHandler);
			}
		}
		log("Restoring state...");
		final EngineServiceAsync engine = GWT.create(EngineService.class);
		engine.getUnits(new EarlCallback<Map<String, String>>() {
			@Override
			public void onSuccess(Map<String, String> result) {
				Set<Entry<String, String>> units = result.entrySet();
				for (Entry<String, String> unit : units) {
					String unitId = unit.getKey();
					String hexId = unit.getValue();
					moveToHex(unitId, hexId);
				}
			}
		});
		OMSVGElement loadingLayer = OMSVGDocument.convert(svg.getElementById("loadingLayer"));
		loadingLayer.getStyle().setVisibility(Visibility.HIDDEN);

		String token = Window.Location.getParameter("token");
		if(token != null) {
			ChannelFactory.createChannel(token, new ChannelCreatedCallback() {
				@Override
				public void onChannelCreated(Channel channel) {
					channel.open(new EarlSocketListener(Earl.this));
				}
			});
		}
		OMSVGGElement roll = OMSVGDocument.convert(svg.getElementById("roll"));
		roll.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				engine.roll(1, 6, new EarlCallback<Integer>() {
					@Override
					public void onSuccess(Integer result) {
						log("1d6:" + result);
					}
				});
			}
		});
	}

	public void moveToHex(String unitId, String hexId) {
		log(unitId + " -> " + hexId);
		OMSVGImageElement svgunit = OMSVGDocument.convert(svg.getElementById(unitId));
		OMSVGPathElement svghex = OMSVGDocument.convert(svg.getElementById(hexId));
		hexHandler.moveToHex(svgunit, svghex);
	}

	public static void log(String s) {
		console(s);
		
		try {
			com.google.gwt.user.client.Element log = DOM.getElementById("log");
			Node text = createTextNode(s + "\n");
			log.appendChild(text);
		} catch (Exception e) {
			console(e);
		}
		try {
			SVGSVGElement svg = getSVG();
			OMSVGTextElement console = OMSVGDocument.convert(svg.getElementById("console"));
			OMSVGDocument svgDoc = OMSVGDocument.convert(svg);
			OMSVGTSpanElement newLine = svgDoc.createSVGTSpanElement();
			newLine.appendChild(svgDoc.createTextNode(s));

			newLine.setAttribute("x", console.getX().getBaseVal().iterator().next().getValueAsString());
			newLine.setAttribute("dy", "15");
			console.appendChild(newLine);
		} catch (Exception e) {
			console(e);
		}
	}		

	public native static Element createTextNode(String text) /*-{
		return $doc.createTextNode(text);
	}-*/;

	public static native void console(Object s) /*-{
		if (console) {
			console.log(s);
		}
	}-*/;


	
	public static native SVGSVGElement getSVG() /*-{
		var s;
		if($wnd.parent.view) {
			//frames
			console.log("frames:"+$wnd.parent.view);
			s = $wnd.parent.view.getSVGDocument();
			if(s) {
				console.log("embed");
			}else{
				console.log("frames");
		 		s = $wnd.parent.view.document;
			}		 	
		}else{
			console.log("inline");
			//inline
			s = $doc.getElementsByTagNameNS("http://www.w3.org/2000/svg", "svg")[0];
		}
	 	console.log("getSVG="+s);
	 	return s;
//		return $wnd.document.getElementById('content').getSVGDocument();
// 		return $doc.getElementById("content").getSVGDocument();
//		return Document.get().getElementsByTagName("svg").getItem(0);
	}-*/;

}
