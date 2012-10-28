package earl.engine.client;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.vectomatic.dom.svg.OMSVGDocument;
import org.vectomatic.dom.svg.OMSVGGElement;
import org.vectomatic.dom.svg.OMSVGImageElement;
import org.vectomatic.dom.svg.OMSVGPathElement;
import org.vectomatic.dom.svg.OMSVGStyle;
import org.vectomatic.dom.svg.impl.SVGGElement;
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
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;

import earl.engine.client.data.GameInfo;
import earl.engine.client.handlers.HexHandler;
import earl.engine.client.handlers.UnitHandler;
import earl.engine.client.utils.EarlCallback;
import earl.engine.client.utils.EarlChannelListener;

public class Earl implements EntryPoint {
	public OMSVGImageElement selectedUnit = null;
	public SVGSVGElement svg;
	public final HexHandler hexHandler = new HexHandler(this);
	public final UnitHandler unitHandler = new UnitHandler(this);
	private EngineServiceAsync engine;
	private GameInfo gameInfo;

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
		RootPanel rootPanel = RootPanel.get("controls");
		rootPanel.getElement().getStyle().setPosition(Position.RELATIVE);
		
		final Button btnNewButton = new Button("Roll 2d6");
		btnNewButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				btnNewButton.setEnabled(false);
				engine.roll(2, 6, new EarlCallback<Integer>(){
					@Override
					public void onSuccess(Integer result) {
						btnNewButton.setEnabled(true);
					}
					@Override
					public void onFailure(Throwable caught) {
						onSuccess(null);
					};					
				});
			}
		});
		rootPanel.add(btnNewButton, 10, 10);
		init();
	}

	public void init() {
		svg = getSVG();
		log("Setting up hexes...");
		NodeList<Element> hexes = svg.getElementById("area").getElementsByTagName("path");
		for (int i = 0; i < hexes.getLength(); ++i) {			
			Element element = hexes.getItem(i);
			SVGPathElement item = (SVGPathElement) element;
			OMSVGPathElement hex = OMSVGDocument.convert(item);
			hex.addMouseOverHandler(hexHandler);
			hex.addMouseOutHandler(hexHandler);
			hex.addClickHandler(hexHandler);
		}
		
		hexes = svg.getElementById("area").getElementsByTagName("g");
		for (int i = 0; i < hexes.getLength(); ++i) {			
			Element element = hexes.getItem(i);
			OMSVGGElement hex = OMSVGDocument.convert((SVGGElement)element);
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
		log("Connecting to game server...");
		engine = GWT.create(EngineService.class);
		engine.connect(new EarlCallback<GameInfo>() {
			@Override
			public void onSuccess(GameInfo result) {
				log("Connected.");
				updateUnits(result.units);
				joinChannel(result.channelToken);
				gameInfo = result;
			}
		});
		log("Ready.");
	}

	protected void joinChannel(final String token) {
		ChannelFactory.createChannel(token, new ChannelCreatedCallback() {
			@Override
			public void onChannelCreated(Channel channel) {
				log("Channel created");
				channel.open(new EarlChannelListener(Earl.this));
			}
		});
	}

	protected void updateUnits(Map<String, String> result) {
		Set<Entry<String, String>> units = result.entrySet();
		for (Entry<String, String> unit : units) {
			String unitId = unit.getKey();
			String hexId = unit.getValue();
			moveToHex(unitId, hexId);
		}
	}

	public void moveToHex(String unitId, String hexId) {
		log(unitId + " -> " + hexId);
		OMSVGImageElement svgunit = OMSVGDocument.convert(svg.getElementById(unitId));
		OMSVGPathElement svghex = OMSVGDocument.convert(svg.getElementById(hexId));
		hexHandler.moveToHex(svgunit, svghex);
	}
	
	public GameInfo getGameInfo() {
		return gameInfo;
	}

	public static void log(String s) {
		console(s);
		try {
			com.google.gwt.user.client.Element log = DOM.getElementById("log");
			Node text = createTextNode(s + "\n");
			log.insertFirst(text);
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
		if ($wnd.parent.view) {
			//frames
			console.log("frames:" + $wnd.parent.view);
			s = $wnd.parent.view.getSVGDocument();
			if (s) {
				console.log("embed");
			} else {
				console.log("frames");
				s = $wnd.parent.view.document;
			}
		} else {
			console.log("inline");
			//inline
			s = $doc
					.getElementsByTagNameNS("http://www.w3.org/2000/svg", "svg")[0];
		}
		console.log("getSVG=" + s);
		return s;
		//		return $wnd.document.getElementById('content').getSVGDocument();
		// 		return $doc.getElementById("content").getSVGDocument();
		//		return Document.get().getElementsByTagName("svg").getItem(0);
	}-*/;
}
