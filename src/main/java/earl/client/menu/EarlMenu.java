package earl.client.menu;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import earl.client.data.Counter;
import earl.client.games.scs.ops.Flip;
import earl.client.games.scs.ops.Move;

public class EarlMenu implements ClickHandler {
	private final EarlClienContext ctx;
	private final RootPanel root;
	private Document log = null;
	private final List<String> logMessages = new ArrayList<String>();

	public EarlMenu(EarlClienContext ctx) {
		this.ctx = ctx;
		root = RootPanel.get("menu");
		Button menu = add("Show menu");
		menu.setVisible(true);
		add("Flip");
		add("Remove unit");
		add("Show log");
		add("Test log");
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
			showPendingLog();
		} else if ("Remove unit".equals(text)) {
			Counter piece = ctx.handler.getSelectedPiece();
			if (piece == null) {
				Window.alert("Select a counter first");
			} else {
				Move move = new Move();
				move.counterRef = piece.ref();
				move.fromRef = piece.getPosition().ref();
				move.toRef = ctx.game.getBoard().getHex("Dead pool").ref();
				ctx.handler.setSelectedPiece(null);
				ctx.display.process(move);
			}
		} else if ("Hide menu".equals(text)) {
			toggleMenu();
			source.setHTML("Show menu");
		} else if ("Flip".equals(text)) {
			Counter piece = ctx.handler.getSelectedPiece();
			if (piece == null) {
				Window.alert("Select a counter first");
			} else {
				Flip op = new Flip(piece.ref());
				ctx.display.process(op);
			}
		} else if ("Test log".equals(text)) {
			for (int i = 1; i < 15; ++i) {
				log(i + ": " + new Date());
			}
		} else if ("Toggle units".equals(text)) {
			toggleVisible(ctx.svg.getElementById("units").getStyle());
			toggleVisible(ctx.svg.getElementById("markers").getStyle());
		} else {
			Window.alert("TODO: " + text);
		}
	}

	public void log(String msg) {
		logMessages.add(msg);
		if (log != null) {
			writeLog(msg);
		}
	}

	public void writeLog(String msg) {
		log.getBody().insertFirst(log.createBRElement());
		log.getBody().insertFirst(log.createTextNode(msg));
	}

	public void showPendingLog() {
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
