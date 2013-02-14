package webboards.client.menu;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import webboards.client.ClientEngine;
import webboards.client.data.CounterInfo;
import webboards.client.data.Game;
import webboards.client.display.BasicDisplay;
import webboards.client.games.Area;
import webboards.client.games.scs.bastogne.Bastogne;
import webboards.client.games.scs.ops.Flip;
import webboards.client.games.scs.ops.Move;
import webboards.client.games.scs.ops.NextPhase;
import webboards.client.ops.Operation;
import webboards.client.ops.Undoable;
import webboards.client.ops.generic.ChatOp;
import webboards.client.ops.generic.DiceRoll;
import webboards.client.ops.generic.UndoOp;
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
		add("Undo Op");
		add("Undo State");
		add("Next phase");
		add("Flip");
		add("Clear traces");
		add("Send msg");
		logBtn = add("Show log");
		add("2d6");
		add("Toggle units"); 
		add("Remove unit");

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
			removeUnit();
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
			flip();
		} else if ("Undo Op".equals(text)) {
			undoOp();
		} else if ("Undo State".equals(text)) {
			undoState();
		} else if ("2d6".equals(text)) {
			DiceRoll roll = new DiceRoll();
			ctx.ctx.process(roll);
		} else if ("Toggle units".equals(text)) {
			toggleVisible(ctx.svg.getElementById("units").getStyle());
			toggleVisible(ctx.svg.getElementById("markers").getStyle());
		} else if ("Next phase".equals(text)) {
			nextPhase();
		} else {
			Window.alert("Not implemented yet: " + text);
		}
	}

	private void nextPhase() {
		NextPhase op = new NextPhase();
		ctx.ctx.process(op);
	}

	private void removeUnit() {
		CounterInfo piece = ctx.ctx.selected;
		if (piece == null) {
			Window.alert("Select a counter first");
		} else {
			Move move = new Move(piece, new Area("Dead pool"));
			ctx.ctx.display.select(null);
			ctx.ctx.process(move);
		}
	}

	private void flip() {
		CounterInfo piece = ctx.ctx.selected;
		if (piece == null) {
			Window.alert("Select a counter first");
		} else {
			Flip op = new Flip(piece.ref());
			ctx.ctx.process(op);
		}
	}

	private void undoState() {
		ServerEngineAsync server = GWT.create(ServerEngine.class);
		long tid = Long.parseLong(ClientEngine.getTableId());
		server.undo(tid, new AbstractCallback<Game>(){
			@Override
			public void onSuccess(Game result) {
				ctx.ctx.ops.remove(ctx.ctx.ops.size() - 1);
				Bastogne bastogne = (Bastogne) result;
				ctx.game = bastogne;
				BasicDisplay display = (BasicDisplay) ctx.ctx.display;
				ctx.ctx.board = result.getBoard();
				display.clearTraces();
				display.updateBoard(ctx.ctx.board);
				for (Operation op: ctx.ctx.ops) {
					op.updateBoard(ctx.ctx.board);
					op.draw(ctx.ctx);
					op.drawDetails(ctx.ctx);
				}
			}
		});
	}
	private void undoOp() {
		Undoable op = findLastOpToUndo();
		if(op == null) {
			Window.alert("Can't undo any more");
			return;			
		}
		if (op instanceof Undoable) {
			UndoOp undo = new UndoOp(op);
			ctx.ctx.ops.remove(op);
			ctx.ctx.process(undo);
		} else {
			Window.alert("Can't undo: " + op);
		}		
	}

	private Undoable findLastOpToUndo() {
		for (int i = ctx.ctx.ops.size() - 1; i >= 0; i--) {
			Operation op = ctx.ctx.ops.get(i);			
			if (op instanceof UndoOp) {
				continue;
			}else if(op instanceof Undoable){
				return (Undoable)  op;
			}
		}
		return null;
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
		var doc = window.open("", "webboards.log", "").document;
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
