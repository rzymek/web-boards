package webboards.client.menu;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.vectomatic.dom.svg.impl.SVGSVGElement;

import webboards.client.ClientEngine;
import webboards.client.ClientOpRunner;
import webboards.client.data.CounterInfo;
import webboards.client.data.GameCtx;
import webboards.client.games.Area;
import webboards.client.games.scs.ops.Flip;
import webboards.client.games.scs.ops.Move;
import webboards.client.ops.Operation;
import webboards.client.ops.Undoable;
import webboards.client.ops.generic.ChatOp;
import webboards.client.ops.generic.UndoOp;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.SimplePanel;

public class ClientMenu extends SimplePanel {
	private final GameCtx ctx;
	private Document log = null;
	private final List<String> logMessages = new ArrayList<String>();
	private final SVGSVGElement svg;
	private boolean expanded = false;
	private ClientOpRunner runner;
	
	private Grid hiddenMenu;
	private Grid menu;
	
	//@formatter:off
	public MenuEntry[] entries = { 
		new MenuEntry("Send msg") {
			@Override
			public void exec() {
				String msg = Window.prompt("Enter message:", "");
				if (msg != null) {
					runner.process(new ChatOp(msg));
				}
			}
		},
		new MenuEntry("Undo") {
			@Override
			public void exec() {
				final Integer opIdx = findLastOpToUndo();
				if (opIdx == null) {
					Window.alert("Can't undo any more");
					return;
				}
				Operation op = ctx.ops.get(opIdx);
				if (!(op instanceof Undoable)) {
					Window.alert("Can't undo " + op);
					return;
				}
				Undoable undoable = (Undoable) op;
				runner.process(new UndoOp(undoable, opIdx));
			}
		},
		new MenuEntry("Refresh") {
			@Override
			public void exec() {
				ClientEngine.reload(ctx);
			}
		},
		new MenuEntry("Show log") {
			@Override
			public void exec() {
				log = openLogWindow();
				showPendingLog();
			}
		},
		new MenuEntry("Remove unit") {
			@Override
			public void exec() {
				CounterInfo piece = ctx.selected;
				if (piece == null) {
					Window.alert("Select a counter first");
				} else {
					Move move = new Move(piece, new Area("Dead pool"));
					ctx.display.select(null);
					runner.process(move);
				}
			}
		},
		new MenuEntry("Clear traces") {
			@Override
			public void exec() {
				ctx.display.clearTraces();
			}
		},
		new MenuEntry("Mark") {
			@Override
			public void exec() {
				if (ctx.selected != null) {
					ctx.display.markUsed(ctx.selected.ref());
				}
			}
		},
		new MenuEntry("Flip") {
			@Override
			public void exec() {
				CounterInfo piece = ctx.selected;
				if (piece == null) {
					Window.alert("Select a counter first");
				} else {
					Flip op = new Flip(piece.ref());
					runner.process(op);
				}
			}
		},
		new MenuEntry("Toggle units") {
			@Override
			public void exec() {
				toggleVisible(svg.getElementById("units").getStyle());
				toggleVisible(svg.getElementById("markers").getStyle());
			}
		},
	};
	//@formatter:on

	public ClientMenu(SVGSVGElement svg, GameCtx ctx) {
		this.svg = svg;
		this.ctx = ctx;
		runner = new ClientOpRunner(ctx);
		
		ButtonMenu menu = new ButtonMenu(
			new MenuEntry("Hide") {
				@Override
				public void exec() {
					setWidget(hiddenMenu);
				}
			}
		);
		
		ButtonMenu hiddenMenu = new ButtonMenu(
			new MenuEntry("Show menu") {
				@Override
				public void exec() {
					setWidget(ClientMenu.this.menu);
				}
			}
		);
		
		menu.addAll(entries);
		this.menu = menu.createVertical();
		this.hiddenMenu = hiddenMenu.createHorizontal();
		setWidget(this.hiddenMenu);
	}

	private Integer findLastOpToUndo() {
		if (ctx.ops.isEmpty()) {
			return null;
		} else {
			Set<Integer> undone = new HashSet<Integer>();
			for (int i = ctx.ops.size() - 1; i >= 0; i--) {
				Operation op = ctx.ops.get(i);
				if (op instanceof UndoOp) {
					UndoOp undo = (UndoOp) op;
					undone.add(undo.opIndex);
					continue;
				}
				if (undone.contains(i)) {
					continue;
				}
				return i;
			}
			return null;
		}
	}

	public void log(String msg) {
		msg = DateTimeFormat.getFormat("[HH:mm:ss] ").format(new Date()) + msg;
		logMessages.add(msg);
		if (log != null) {
			writeLog(msg);
		}
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
}
