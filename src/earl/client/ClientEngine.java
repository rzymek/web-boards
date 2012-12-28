package earl.client;

import org.vectomatic.dom.svg.OMDocument;
import org.vectomatic.dom.svg.OMSVGSVGElement;
import org.vectomatic.dom.svg.impl.SVGSVGElement;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchEndHandler;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;

import earl.client.bastogne.op.ChatMessage;
import earl.client.bastogne.op.DiceRoll;
import earl.client.data.Board;
import earl.client.data.GameInfo;
import earl.client.display.handler.BasicDisplayHandler;
import earl.client.display.handler.SCSDisplayHandler;
import earl.client.display.svg.SVGDisplay2;
import earl.client.games.Bastogne;
import earl.client.op.OpData;
import earl.client.op.Operation;
import earl.client.remote.ServerEngine;
import earl.client.remote.ServerEngineAsync;
import earl.client.utils.AbstractCallback;
import earl.client.utils.Browser;

public class ClientEngine implements EntryPoint {
	private SVGDisplay2 display;
	private SVGSVGElement svg;
	protected Board board;
	private ServerEngineAsync service;

	@Override
	public void onModuleLoad() {
		RootPanel rootPanel = RootPanel.get("body");
		if(rootPanel == null) {
			return;//ugly hack
		}
		testTouch(rootPanel);
		
		svg = getSVG();
		zoomAndPan(rootPanel);
		connect();
		bindButtons();
		Window.setTitle("Bastogne!");
		log("Started");
	}

	protected void zoomAndPan(RootPanel rootPanel) {
		OMSVGSVGElement omsvg = OMDocument.convert(svg);
		SVGZoomAndPanHandler zoomAndPan = new SVGZoomAndPanHandler(svg);
		omsvg.addMouseDownHandler(zoomAndPan);
		omsvg.addMouseUpHandler(zoomAndPan);
		omsvg.addMouseMoveHandler(zoomAndPan);
		rootPanel.addDomHandler(zoomAndPan, MouseWheelEvent.getType());
	}

	protected void bindButtons() {
		Button.wrap(Document.get().getElementById("roll2d6")).addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				DiceRoll op = new DiceRoll();
				op.dice = 2;
				op.sides = 6;
				display.process(op);
			}
		});
		Button.wrap(Document.get().getElementById("rolld6")).addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				log("Rolling d6...");
				DiceRoll op = new DiceRoll();
				op.dice = 1;
				op.sides = 6;
				service.process(op, new LogExecutedOperation(ClientEngine.this, op));
			}
		});
		
		final TextBox chat = TextBox.wrap(Document.get().getElementById("chat"));
		chat.addKeyPressHandler(new KeyPressHandler() {			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				char code = event.getCharCode();
				if(code == '\r' || code == '\n') {
					final ChatMessage op = new ChatMessage();
					op.text = chat.getText();
					service.process(op, new LogExecutedOperation(ClientEngine.this, op));
					chat.setText("");
				}
			}
		});
		Button.wrap(Document.get().getElementById("debug")).addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				log("debug");
			}
		});
		Button.wrap(Document.get().getElementById("deselect")).addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				log("debug");
//				((SVGDisplay)display).getDisplayHandler().setSelectedPiece(null);
			}
		});
		Button.wrap(Document.get().getElementById("toggle")).addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
//				display.toggleUnits();
			}
		});
		Button.wrap(Document.get().getElementById("flip")).addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
//				Counter piece = display.getDisplayHandler().getSelectedPiece();
//				piece.flip();
//				SVGImageElement c = (SVGImageElement) svg.getElementById(piece.getId());
//				c.getHref().setBaseVal(piece.getState());
			}
		});
		Button.wrap(Document.get().getElementById("mark")).addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
//				SVGElement selected = ((SVGDisplay)display).getSelected();
//				if (selected != null) {
//					Style style = selected.getStyle();
//					try {
//						double opacity = Double.parseDouble(style.getOpacity());
//						opacity = (opacity < 0.9 ? 1.0 : 0.6);
//						style.setOpacity(opacity);
//					} catch (NumberFormatException ex) {
//						style.setOpacity(0.6);
//					}
//				}
			}
		});
	}

	protected void connect() {
		service = GWT.create(ServerEngine.class);
		final String tableId = Window.Location.getParameter("table");
		service.getState(tableId, new AbstractCallback<GameInfo>(){
			@Override
			public void onSuccess(GameInfo info) {					
				Bastogne game = new Bastogne();
				game.setupScenarion52();
				game.setMapInfo(info.mapInfo);
				BasicDisplayHandler handler = new SCSDisplayHandler(game);
				display = new SVGDisplay2(svg, handler);
				ClientEngine.this.display = display;
				board = game.getBoard();
				display.setBoard(board);
				for (OpData data : info.ops) {
					Operation op= data.get(board);					
					op.clientExecute();
					op.draw(display);
					log(op.toString());
				}
				if(info.joinAs != null) {
					boolean yes = Window.confirm("Would you like to join this game as " + info.joinAs);
					if (yes) {
						service.join(tableId, new AbstractCallback<Void>());
					}
				}
			}
		});
	}

	protected void testTouch(RootPanel rootPanel) {
		rootPanel.addDomHandler(new TouchStartHandler() {			
			@Override
			public void onTouchStart(TouchStartEvent event) {
				log("1 touch start "+Window.getClientWidth());
				Document.get().getElementById("menu").getStyle().setDisplay(Display.NONE);
			}
		}, TouchStartEvent.getType());
		rootPanel.addDomHandler(new TouchEndHandler() {			
			@Override
			public void onTouchEnd(TouchEndEvent event) {				
				log("2 touch end "+getInfo());
				Document.get().getElementById("menu").getStyle().setDisplay(Display.BLOCK);
			}
		}, TouchEndEvent.getType());
	}
	public static native String getInfo()/*-{
		var x =+$doc.getElementById('viewport.x');
		var w =+$doc.getElementById('viewport.width');
		return "iw="+$wnd.innerWidth+", pXo="+$wnd.pageXOffset+
			", screen.width="+screen.width+", "+
			", viewport.x="+x.offsetLeft+", viewport.width="+w.clientWidth;
	}-*/;

	public static void log(String s) {
		Browser.console(s);
		GWT.log(s);
		try {
			Element log = DOM.getElementById("log");
			Node text = Browser.createTextNode(s + "\n");
			log.insertFirst(text);
		} catch (Exception e) {
			GWT.log(s, e);
			Browser.console(e);
		}
	}

	public static native SVGSVGElement getSVG() /*-{
 		return $doc.getElementsByTagNameNS("http://www.w3.org/2000/svg", "svg")[0];
	}-*/;
}
