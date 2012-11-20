package earl.client;

import org.vectomatic.dom.svg.impl.SVGElement;
import org.vectomatic.dom.svg.impl.SVGSVGElement;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.Style;
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
		String tableId = Window.Location.getParameter("table");
		service.getState(tableId, new AbstractCallback<GameInfo>(){

			@Override
			public void onSuccess(GameInfo info) {					
				Display display = new SVGDisplay(getSVG(), info.game);
				ClientEngine.this.display = display;
				display.addGameListener(new UpdateServer(service));
				display.init(info.game.getBoard());
				log(info.log);
			}
		});
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
		Button.wrap(Document.get().getElementById("rolld6")).addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				log("Rolling d6...");
				service.roll(1, 6, new AbstractCallback<Integer>(){
					@Override
					public void onSuccess(Integer result) {
						log("d6 = "+result);
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
		Button.wrap(Document.get().getElementById("deselect")).addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				log("debug");
				((SVGDisplay)display).getDisplayHandler().setSelectedPiece(null);
			}
		});
		Button.wrap(Document.get().getElementById("toggle")).addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				display.toggleUnits();
			}
		});
		Button.wrap(Document.get().getElementById("mark")).addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				SVGElement selected = ((SVGDisplay)display).getSelected();
				if (selected != null) {
					Style style = selected.getStyle();
					try {
						double opacity = Double.parseDouble(style.getOpacity());
						opacity = (opacity < 0.9 ? 1.0 : 0.6);
						style.setOpacity(opacity);
					} catch (NumberFormatException ex) {
						style.setOpacity(0.6);
					}
				}
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
