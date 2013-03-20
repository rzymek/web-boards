package webboards.client;

import org.vectomatic.dom.svg.impl.SVGImageElement;
import org.vectomatic.dom.svg.impl.SVGSVGElement;

import webboards.client.data.GameCtx;
import webboards.client.data.GameInfo;
import webboards.client.display.svg.SVGDisplay;
import webboards.client.display.svg.SVGZoomAndPanHandler;
import webboards.client.display.svg.edit.EditDisplay;
import webboards.client.display.svg.edit.EmptyScenario;
import webboards.client.games.scs.bastogne.Bastogne;
import webboards.client.games.scs.ops.NextPhase;
import webboards.client.menu.ClientMenu;
import webboards.client.ops.ClearScreen;
import webboards.client.ops.Operation;
import webboards.client.remote.ServerEngine;
import webboards.client.remote.ServerEngineAsync;
import webboards.client.utils.AbstractCallback;
import webboards.client.utils.Browser;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

public class ClientEngine implements EntryPoint {
	private SVGSVGElement svg;
	private ServerEngineAsync service;
	private static ClientMenu menu;
 

	@Override
	public void onModuleLoad() {
		svg = getSVG();
		exceptionHandler();

		SVGImageElement board = (SVGImageElement) svg.getElementById("img");
		board.getHref().setBaseVal(Resources.INSTANCE.board().getSafeUri().asString());

		SVGZoomAndPanHandler.attach(svg);
		if (Window.Location.getParameter("editor") != null) {
			EditDisplay display = new EditDisplay(svg);
			Bastogne game = new Bastogne();
			display.setBoard(game.start(new EmptyScenario()));
		}else{
			connect();
		}
		if(!isTouchDevice()) {
//			int width = (int) svg.getViewBox().getBaseVal().getWidth();
//			int height = (int) svg.getViewBox().getBaseVal().getHeight();
//			log("setting width:height to "+width+":"+height);
			String width = "100%";
			String height = "100%";
			svg.getWidth().getBaseVal().setValueAsString(width+"");
			svg.getHeight().getBaseVal().setValueAsString(height+"");
		}
		Window.setTitle("Bastogne!");
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


	public void start(final long tableId, final GameInfo info) {
		final GameCtx ctx = new GameCtx();
		ctx.setInfo(info);
		ctx.board = info.game.start(info.scenario);
		SVGDisplay display = new SVGDisplay(svg, ctx);
		menu = new ClientMenu(svg, ctx);
		display.setBoard(ctx.board);
		new NextPhase().draw(ctx);

		NotificationListener listener = new NotificationListener(ctx);
		listener.join(info.channelToken);
		
		if(info.joinAs != null) {
			boolean yes = Window.confirm("Would you like to join this game as " + info.joinAs);
			if (yes) {
				service.join(tableId, info.joinAs, new AbstractCallback<Void>(){
					@Override
					public void onSuccess(Void result) {
						log("Joined as " + ctx.side);		
						ctx.side = info.joinAs;
						update(ctx);
					}
				});
			}
		}else{
			log("Playing as " + ctx.side);		
			update(ctx);
		}
	}

	public static void update(GameCtx ctx) {
		int startDetailsFrom = ctx.ops.size()-30;
		for (int i = ctx.ops.size()-1; i>=0; --i) {
			Operation op = ctx.ops.get(i);
			if(op instanceof ClearScreen) {
				startDetailsFrom = i;
				break;
			}
		}
		
		for (int i = 0; i < ctx.ops.size(); ++i) {
			Operation op = ctx.ops.get(i);
			op.updateBoard(ctx.board);
			op.postServer(ctx);			
			op.draw(ctx);
			if(i >= startDetailsFrom) {
				op.drawDetails(ctx);
			}
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


	protected void connect() {
		service = GWT.create(ServerEngine.class);
		final long tableId = getTableId();
		service.getState(tableId, new AbstractCallback<GameInfo>(){
			@Override
			public void onSuccess(GameInfo info) {					
				start(tableId, info);
			}
		});
	}

	public static long getTableId() {
		return Long.parseLong(Window.Location.getParameter("table"));
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
