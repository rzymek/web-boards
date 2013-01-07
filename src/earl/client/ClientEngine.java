package earl.client;

import org.vectomatic.dom.svg.OMDocument;
import org.vectomatic.dom.svg.OMSVGSVGElement;
import org.vectomatic.dom.svg.impl.SVGSVGElement;

import com.google.gwt.appengine.channel.client.Channel;
import com.google.gwt.appengine.channel.client.ChannelFactory;
import com.google.gwt.appengine.channel.client.ChannelFactory.ChannelCreatedCallback;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.Node;
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
import earl.client.display.svg.SVGDisplay;
import earl.client.display.svg.edit.EditDisplay;
import earl.client.games.Bastogne;
import earl.client.op.OpData;
import earl.client.op.Operation;
import earl.client.remote.ServerEngine;
import earl.client.remote.ServerEngineAsync;
import earl.client.utils.AbstractCallback;
import earl.client.utils.Browser;

public class ClientEngine implements EntryPoint {
	private SVGDisplay display;
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
		if (Window.Location.getParameter("editor") != null) {
			display = new EditDisplay(svg);
			Bastogne game = new Bastogne();
			display.setBoard(game.getBoard());
		}else{
			connect();
		}
		bindButtons();
		Window.setTitle("Bastogne!");
		centerView();
		RootPanel.get().addDomHandler(new KeyPressHandler() {			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				char c = event.getCharCode();
				if(GWT.isProdMode() && c=='z') {
					Browser.console(event);
					String url = Window.Location.getHref();
					url += url.contains("?") ? '&' : '?';
					Window.Location.replace(url+"gwt.codesvr="+Window.Location.getHostName()+":9997");
				}
			}
		}, KeyPressEvent.getType());
	}

	private void centerView() {
		if(isTouchDevice()) {
			return;
		}
		int w = Document.get().getClientWidth();
		float x = svg.getViewBox().getBaseVal().getCenterX() - w/2;
		svg.getViewBox().getBaseVal().setX(x);
		int h = Document.get().getClientHeight();
		float y = svg.getViewBox().getBaseVal().getCenterY() - h/2;
		svg.getViewBox().getBaseVal().setY(y);
	}

	private native boolean isTouchDevice() /*-{
		return 'ontouchstart' in $wnd || 'onmsgesturechange' in $wnd;
	}-*/;

	protected void zoomAndPan(RootPanel rootPanel) {
		OMSVGSVGElement omsvg = OMDocument.convert(svg);
		SVGZoomAndPanHandler zoomAndPan = new SVGZoomAndPanHandler(svg);
		omsvg.addMouseDownHandler(zoomAndPan);
		omsvg.addMouseUpHandler(zoomAndPan);
		omsvg.addMouseMoveHandler(zoomAndPan);
		RootPanel.get().addDomHandler(zoomAndPan, MouseWheelEvent.getType());
		RootPanel.get().addDomHandler(zoomAndPan, KeyPressEvent.getType());
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
		Button.wrap(Document.get().getElementById("flip")).addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
//				Counter piece = display.getDisplayHandler().getSelectedPiece();
//				piece.flip();
//				SVGImageElement c = (SVGImageElement) svg.getElementById(piece.getId());
//				c.getHref().setBaseVal(piece.getState());
			}
		});
	}

	protected void connect() {
		service = GWT.create(ServerEngine.class);
		final String tableId = Window.Location.getParameter("table");
		service.getState(tableId, new AbstractCallback<GameInfo>(){
			@Override
			public void onSuccess(GameInfo info) {					
				start(tableId, info);
			}
		});
	}

	protected void testTouch(RootPanel rootPanel) {
		rootPanel.addDomHandler(new TouchStartHandler() {			
			@Override
			public void onTouchStart(TouchStartEvent event) {
//				log("1 touch start "+Window.getClientWidth());
//				Document.get().getElementById("menu").getStyle().setDisplay(Display.NONE);
			}
		}, TouchStartEvent.getType());
		rootPanel.addDomHandler(new TouchEndHandler() {			
			@Override
			public void onTouchEnd(TouchEndEvent event) {				
//				Document.get().getElementById("menu").getStyle().setDisplay(Display.BLOCK);
				ImageElement img = (ImageElement) Document.get().getElementById("menuimg");
				log("2 touch end "+getInfo());
				int w = getViewportWidth();
				img.setWidth(w/8);
				img.setHeight(w/8);
			}
		}, TouchEndEvent.getType());
	}
	private void start(final String tableId, GameInfo info) {
		final Bastogne game = new Bastogne();
		game.setupScenarion52();
		game.setMapInfo(info.mapInfo);
		BasicDisplayHandler handler = new SCSDisplayHandler(game, info.side);
		display = new SVGDisplay(svg, handler);
		ClientEngine.this.display = display;
		board = game.getBoard();
		display.setBoard(board);
		if (Window.Location.getParameter("i") != null) {
			ChannelFactory.createChannel(info.channelToken, new ChannelCreatedCallback() {					
				@Override
				public void onChannelCreated(Channel channel) {
					channel.open(new NotificationListener(game.getBoard(), display));						
				}
			});
		}
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

	public static native int getViewportWidth()/*-{
		var w =+$doc.getElementById('viewport.width');
		return w.clientWidth;
	}-*/;
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
