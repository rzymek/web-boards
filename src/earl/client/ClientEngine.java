package earl.client;

import org.vectomatic.dom.svg.impl.SVGSVGElement;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Node;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextBox;

import earl.client.data.GameInfo;
import earl.client.display.Display;
import earl.client.display.svg.SVGDisplay;
import earl.client.games.Bastogne;
import earl.client.remote.ServerEngine;
import earl.client.remote.ServerEngineAsync;
import earl.client.utils.AbstractCallback;
import earl.client.utils.Browser;

public class ClientEngine implements EntryPoint {
	private Display display;

	@Override
	public void onModuleLoad() {
//		RootPanel rootPanel = RootPanel.get("controls");

		final ServerEngineAsync service = GWT.create(ServerEngine.class);
		if(isDemo()) {
			log("Demo mode");
			Display display = new SVGDisplay(getSVG());
			Bastogne bastogne = new Bastogne();
			bastogne.setupScenarion52();
			display.init(bastogne.getBoard());
		} else { 
			String tableId = Window.Location.getParameter("table");
			service.getState(tableId, new AbstractCallback<GameInfo>(){

				@Override
				public void onSuccess(GameInfo info) {					
					Display display = new SVGDisplay(getSVG());
					ClientEngine.this.display = display;
					display.addGameListener(new UpdateServer(service));
					display.init(info.game.getBoard());
					log(info.log);
				}
			});
		}
		Button.wrap(Document.get().getElementById("roll2d6")).addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				log("Rolling 2d6...");
				service.roll(2, 6, new AbstractCallback<Integer>(){
					@Override
					public void onSuccess(Integer result) {
						log("2d6 = "+result);
					}
				});
			}
		});
		
		final TextBox chat = TextBox.wrap(Document.get().getElementById("chat"));
		chat.addKeyPressHandler(new KeyPressHandler() {			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				char code = event.getCharCode();
				if(code == '\r' || code == '\n') {
					final String text = chat.getText();
					service.chat(text, new AbstractCallback<Void>(){
						@Override
						public void onSuccess(Void result) {
							log(">>>"+text);
						}
					});
					chat.setText("");
				}
			}
		});
		Button.wrap(Document.get().getElementById("debug")).addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				log("debug");
				((SVGDisplay)display).board.debug();
			}
		});
		log("Started");
	}

	private boolean isDemo() {
		return Window.Location.getPath().contains("/demo");
	}

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
