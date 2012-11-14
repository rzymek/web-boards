package earl.client;

import org.vectomatic.dom.svg.impl.SVGSVGElement;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Node;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;

import earl.client.data.Board;
import earl.client.display.Display;
import earl.client.display.svg.SVGDisplay;
import earl.client.remote.ServerEngine;
import earl.client.remote.ServerEngineAsync;
import earl.client.utils.AbstractCallback;
import earl.client.utils.Browser;

public class ClientEngine implements EntryPoint {

	@Override
	public void onModuleLoad() {
//		RootPanel rootPanel = RootPanel.get("controls");

		final ServerEngineAsync service = GWT.create(ServerEngine.class);
		String tableId = Window.Location.getParameter("table");
		service.getState(tableId, new AbstractCallback<Board>(){
			@Override
			public void onSuccess(Board board) {
				Display display = new SVGDisplay(getSVG());
				display.init(board);
			}
		});
		Button roll2d6 = Button.wrap(Document.get().getElementById("roll2d6"));
		roll2d6.addClickHandler(new ClickHandler() {			
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
		log("Started");
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
			s = $doc.getElementsByTagNameNS("http://www.w3.org/2000/svg", "svg")[0];
		}
		console.log("getSVG=" + s);
		return s;
		//		return $wnd.document.getElementById('content').getSVGDocument();
		// 		return $doc.getElementById("content").getSVGDocument();
		//		return Document.get().getElementsByTagName("svg").getItem(0);
	}-*/;
}
