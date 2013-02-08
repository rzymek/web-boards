package webboards.client.menu;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import webboards.client.ClientEngine;
import webboards.client.data.GameInfo;
import webboards.client.ops.generic.ChatOp;
import webboards.client.ops.generic.DiceRoll;
import webboards.client.remote.ServerEngine;
import webboards.client.remote.ServerEngineAsync;
import webboards.client.utils.AbstractCallback;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.FontStyle;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class EarlMenu implements ClickHandler {
	private final EarlClienContext ctx;
	private final RootPanel root;
	private Document log = null;
	private final List<String> logMessages = new ArrayList<String>();
	private final Button logBtn;

	public EarlMenu(EarlClienContext ctx) {
		this.ctx = ctx;
		root = RootPanel.get("menu");
		Button menu = add("Show menu");
		menu.setVisible(true);
		add("Undo");
		add("Flip");
		add("Clear traces");
		add("Send msg");
		add("Remove unit");
		logBtn = add("Show log");
		add("2d6");
		add("Toggle units");

		log("Playing as " + ctx.side);
	}

	private Button add(String text) {
		Button button = new Button(text);
		button.setVisible(false);
		button.addClickHandler(this);
		root.add(button);
		return button;
	}

	@Override
	public void onClick(ClickEvent event) {
		final Button source = (Button) event.getSource();
		String text = source.getHTML();
		if ("Show menu".equals(text)) {
			toggleMenu();
			source.setHTML("Hide menu");
		} else if ("Show log".equals(text)) {
			log = openLogWindow();
			logBtn.getElement().getStyle().setFontStyle(FontStyle.NORMAL);
			showPendingLog();
		} else if ("Remove unit".equals(text)) {
//			Counter piece = ctx.handler.getSelectedPiece();
//			if (piece == null) {
//				Window.alert("Select a counter first");
//			} else {
//				Move move = new Move(piece, new AreaRef("Dead pool"));
//				ctx.handler.setSelectedPiece(null);
//				ctx.display.process(move);
//			}
		} else if ("Send msg".equals(text)) {
			String msg = Window.prompt("Enter message:", "");
			if(msg != null) {
				ctx.ctx.process(new ChatOp(msg));
			}
		} else if ("Clear traces".equals(text)) {
			ctx.ctx.display.clearTraces();
		} else if ("Hide menu".equals(text)) {
			toggleMenu();
			source.setHTML("Show menu");
		} else if ("Flip".equals(text)) {
//			Counter piece = ctx.handler.getSelectedPiece();
//			if (piece == null) {
//				Window.alert("Select a counter first");
//			} else {
//				Flip op = new Flip(piece.ref());
//				ctx.display.process(op);
//			}
		} else if ("Undo".equals(text)) {
			ServerEngineAsync server = GWT.create(ServerEngine.class);
			server.undo(Long.valueOf(ClientEngine.getTableId()), new AbstractCallback<GameInfo>(){
				@Override
				public void onSuccess(GameInfo result) {
					ctx.engine.update(result);
				}
			});
		} else if ("2d6".equals(text)) {
			DiceRoll roll = new DiceRoll();
			ctx.ctx.process(roll);
		} else if ("Toggle units".equals(text)) {
			toggleVisible(ctx.svg.getElementById("units").getStyle());
			toggleVisible(ctx.svg.getElementById("markers").getStyle());
		} else {
			Window.alert("Not implemented yet: " + text);
		}
	}

	public void log(String msg) {
		msg = DateTimeFormat.getFormat("[HH:mm:ss] ").format(new Date()) + msg;
		logMessages.add(msg);
		if (log != null) {
			writeLog(msg);
		}
		logBtn.getElement().getStyle().setFontStyle(FontStyle.ITALIC);
	}

	private void writeLog(String msg) {
		log.getBody().insertFirst(log.createBRElement());
		log.getBody().insertFirst(log.createTextNode(msg));
	}

	private void showPendingLog() {
		if (log.getBody().getChildCount() == 0) {
			for (String line : logMessages) {
				writeLog(line);
			}
		}
	}

	private static native Document openLogWindow() /*-{
		var doc = window.open("", "earl.log", "").document;
		doc.title = "Game log";
		return doc;
	}-*/;

	private void toggleVisible(Style style) {
		String visibility = style.getVisibility();
		String hidden = Visibility.HIDDEN.getCssName();
		if (hidden.equals(visibility)) {
			style.setVisibility(Visibility.VISIBLE);
		} else {
			style.setVisibility(Visibility.HIDDEN);
		}
	}

	private void toggleMenu() {
		for (int i = 1; i < root.getWidgetCount(); i++) {
			Widget w = root.getWidget(i);
			w.setVisible(!w.isVisible());
		}
	}
}
