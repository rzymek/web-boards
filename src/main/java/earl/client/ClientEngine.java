package earl.client;

import java.util.Map.Entry;
import java.util.Set;

import org.vectomatic.dom.svg.OMDocument;
import org.vectomatic.dom.svg.OMSVGSVGElement;
import org.vectomatic.dom.svg.impl.SVGSVGElement;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

import earl.client.data.Board;
import earl.client.data.CounterInfo;
import earl.client.data.GameCtx;
import earl.client.data.GameInfo;
import earl.client.display.svg.SVGDisplay;
import earl.client.display.svg.SVGZoomAndPanHandler;
import earl.client.display.svg.edit.EditDisplay;
import earl.client.games.Position;
import earl.client.games.scs.bastogne.Bastogne;
import earl.client.games.scs.ops.Move;
import earl.client.menu.EarlClienContext;
import earl.client.menu.EarlMenu;
import earl.client.ops.Operation;
import earl.client.remote.ServerEngine;
import earl.client.remote.ServerEngineAsync;
import earl.client.utils.AbstractCallback;
import earl.client.utils.Browser;

public class ClientEngine implements EntryPoint {
	private SVGDisplay display;
	private SVGSVGElement svg;
	protected Board board;
	private ServerEngineAsync service;
	private static EarlMenu menu;

	@Override
	public void onModuleLoad() {		
		exceptionHandler();
		svg = getSVG();
		setupZoomAndPan();
		if (Window.Location.getParameter("editor") != null) {
			display = new EditDisplay(svg);
			Bastogne game = new Bastogne();
			display.setBoard(game.getBoard());
		}else{
			connect();
		}
		if(isTouchDevice()) {
			int width = (int) svg.getViewBox().getBaseVal().getWidth();
			int height = (int) svg.getViewBox().getBaseVal().getHeight();
			log("setting width:height to "+width+":"+height);
			svg.getWidth().getBaseVal().setValueAsString(width+"");
			svg.getHeight().getBaseVal().setValueAsString(height+"");
		}
		Window.setTitle("Bastogne!");
//		centerView();
		setupKeys();
	}


	public void exceptionHandler() {
		com.google.gwt.core.client.GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {			
			@Override
			public void onUncaughtException(Throwable e) {
				AbstractCallback.handle(e);
			}
		});
	}


	public void start(final String tableId, GameInfo info) {
		display = new SVGDisplay(svg);
		ClientEngine.this.display = display;
		board =info. game.getBoard();
		display.setBoard(board);
		
		EarlClienContext ctx = new EarlClienContext();
		ctx.svg = svg;
		ctx.game = (Bastogne) info.game;
		ctx.side = info.side;
		ctx.ctx = new GameCtx();
		ctx.ctx.display = display;
		ctx.ctx.board = ctx.game.getBoard();
		ctx.engine = this;
		
		menu = new EarlMenu(ctx);

		NotificationListener listener = new NotificationListener(ctx.ctx);
		listener.join(info.channelToken);
		
		update(info);
		if(info.joinAs != null) {
			boolean yes = Window.confirm("Would you like to join this game as " + info.joinAs);
			if (yes) {
				service.join(tableId, new AbstractCallback<Void>());
			}
		}
	}


	public void update(GameInfo info) {
		Set<Entry<String, Position>> set = info.state.entrySet();
		for (Entry<String, Position> state : set) {
			String counterId = state.getKey();
			Position pos = state.getValue();
			CounterInfo counter = board.getCounter(counterId);
			Move move = new Move(counter, pos);
			move.updateBoard(board);
			move.draw(display.getCtx());
		}
		for (Operation op : info.ops) {
			op.updateBoard(board);
			op.drawDetails(display);
			op.draw(display.getCtx());
			op.postServer(display.getCtx());
			log(op.toString());
		}
	}


	public void setupKeys() {
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

	private native boolean isViewportScaling() /*-{
		console.log($doc.documentElement.scrollWidth);
		console.log($wnd.innerWidth);
		return $doc.documentElement.scrollWidth <= $wnd.innerWidth;
	}-*/;

	private native boolean isTouchDevice() /*-{
		return 'ontouchstart' in $wnd || 'onmsgesturechange' in $wnd;
	}-*/;
	
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


	protected void setupZoomAndPan() {
		OMSVGSVGElement omsvg = OMDocument.convert(svg);
		SVGZoomAndPanHandler zoomAndPan = new SVGZoomAndPanHandler(svg);
		omsvg.addMouseDownHandler(zoomAndPan);
		omsvg.addMouseUpHandler(zoomAndPan);
		omsvg.addMouseMoveHandler(zoomAndPan);
		RootPanel.get().addDomHandler(zoomAndPan, MouseWheelEvent.getType());
		RootPanel.get().addDomHandler(zoomAndPan, KeyPressEvent.getType());
	}
	
	protected void connect() {
		service = GWT.create(ServerEngine.class);
		final String tableId = getTableId();
		service.getState(tableId, new AbstractCallback<GameInfo>(){
			@Override
			public void onSuccess(GameInfo info) {					
				start(tableId, info);
			}
		});
	}

	public static String getTableId() {
		return Window.Location.getParameter("table");
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
		if(menu != null) {
			menu.log(s);
		}
	}

	public static native SVGSVGElement getSVG() /*-{
 		return $doc.getElementsByTagNameNS("http://www.w3.org/2000/svg", "svg")[0];
	}-*/;
}
