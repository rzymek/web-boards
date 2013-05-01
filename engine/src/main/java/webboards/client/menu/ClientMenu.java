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
import webboards.client.display.BasicDisplay;
import webboards.client.display.svg.SVGDisplay;
import webboards.client.games.Area;
import webboards.client.games.Hex;
import webboards.client.games.scs.ops.Flip;
import webboards.client.games.scs.ops.Move;
import webboards.client.games.scs.ops.NextPhase;
import webboards.client.ops.Operation;
import webboards.client.ops.Undoable;
import webboards.client.ops.generic.ChatOp;
import webboards.client.ops.generic.DiceRoll;
import webboards.client.ops.generic.UndoOp;
import webboards.client.remote.ServerEngine;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.FontStyle;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamFactory;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class ClientMenu implements ClickHandler {
	private final GameCtx ctx;
	private final RootPanel root;
	private Document log = null;
	private final List<String> logMessages = new ArrayList<String>();
	private final Button logBtn;
	private final SVGSVGElement svg;
	private boolean expanded = false;
	private ClientOpRunner runner;

	public ClientMenu(SVGSVGElement svg, GameCtx ctx) {
		this.svg = svg;
		this.ctx = ctx;
		runner = new ClientOpRunner(ctx);
		root = RootPanel.get("menu");
		Button menu = add("Show menu");
		menu.setVisible(true);
		add("Undo Op");
		add("Next phase");
		add("Mark used");
		add("Flip");
		add("Clear traces");
		add("Send msg");
		logBtn = add("Show log");
		add("2d6");
		add("Toggle units"); 
		add("Remove unit");
		add("DG");
		add("Expand");
		add("Refresh");
		add("Verify");
		add("Generate");		
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
				runner.process(new ChatOp(msg));
			}
		} else if ("Clear traces".equals(text)) {
			ctx.display.clearTraces();
		} else if ("Hide menu".equals(text)) {
			toggleMenu();
			source.setHTML("Show menu");
		} else if ("Expand".equals(text)) {
			root.setWidth(expanded ? "45px" : "90px");
			expanded = !expanded;
		} else if ("Mark used".equals(text)) {
			if(ctx.selected != null) {
				ctx.display.markUsed(ctx.selected.ref());
			}
		} else if ("Flip".equals(text)) {
			flip();
		} else if ("Verify".equals(text)) {
			SVGDisplay d = (SVGDisplay) ctx.display;
			d.verify();
		} else if ("Refresh".equals(text)) {
			historyIndex = null;
			ClientEngine.reload(ctx);
		} else if ("Undo Op".equals(text)) {
			undoOp();
		} else if ("2d6".equals(text)) {
			DiceRoll roll = new DiceRoll();
			try {
				SerializationStreamFactory f = GWT.create(ServerEngine.class);			
				SerializationStreamWriter w = f.createStreamWriter();
				w.writeObject(roll);
				String s = w.toString();
				Window.alert("roll:"+s);
				SerializationStreamReader r = f.createStreamReader(s);
				Operation op = (Operation) r.readObject();
				runner.process(op);
			} catch (SerializationException e) {
				Window.alert(e.toString());
			}			
//			ctx.process(roll);
		} else if ("Toggle units".equals(text)) {
			toggleVisible(svg.getElementById("units").getStyle());
			toggleVisible(svg.getElementById("markers").getStyle());
		} else if ("Next phase".equals(text)) {
			nextPhase();
		}else if("Generate".equals(text)) {
			String prompt = Window.prompt("Turns:", "300");
			int count = Integer.parseInt(prompt);
			generate(count);
		}else if("&lt;".equals(text)) {
			back();
		}else if("&gt;".equals(text)) {
			forward();
		}else if("&gt;|".equals(text)) {
			historyIndex = ctx.ops.size();
		} else {
			Window.alert("Not implemented yet: " + text);
		}
	}

	private void forward() {
		if(historyIndex == null || historyIndex >= ctx.ops.size())  {
			return;
		}
		Operation op = ctx.ops.get(historyIndex);			
		op.updateBoard(ctx.board);
		op.draw(ctx);
		op.postServer(ctx);			
		op.drawDetails(ctx);
		
		historyIndex++;
	}

	private Integer historyIndex = null;
	
	private void back() {
		ctx.display.clearTraces();
		ctx.board = ctx.info.game.start(ctx.info.scenario);		
		BasicDisplay display = (BasicDisplay) ctx.display;
		display.updateBoard(ctx.board);runner = new ClientOpRunner(ctx);
		if(historyIndex == null) {
			historyIndex = ctx.ops.size();
		}else{
			historyIndex--;
		}
		int max = historyIndex;
		ctx.display.clearTraces();
		for (int i = 0; i < max; ++i) {
			Operation op = ctx.ops.get(i);			
			op.updateBoard(ctx.board);
			op.draw(ctx);
			op.postServer(ctx);			
			if(i > max-5) {
				op.drawDetails(ctx);
			}
		}
	}

	private void nextPhase() {
		NextPhase op = new NextPhase();
		runner.process(op);
	}

	private void removeUnit() {
		CounterInfo piece = ctx.selected;
		if (piece == null) {
			Window.alert("Select a counter first");
		} else {
			Move move = new Move(piece, new Area("Dead pool"));
			ctx.display.select(null);
			runner.process(move);
		}
	}

	private void flip() {
		CounterInfo piece = ctx.selected;
		if (piece == null) {
			Window.alert("Select a counter first");
		} else {
			Flip op = new Flip(piece.ref());
			runner.process(op);
		}
	}

	private void undoOp() {
		final Integer opIdx = findLastOpToUndo();
		if(opIdx == null) {
			Window.alert("Can't undo any more");
			return;		
		}
		Operation op = ctx.ops.get(opIdx);
		if(!(op instanceof Undoable)) {
			Window.alert("Can't undo "+op);
			return;
		}
		Undoable undoable = (Undoable) op;
		runner.process(new UndoOp(undoable, opIdx));
	}

	private Integer findLastOpToUndo() {
		if(ctx.ops.isEmpty()) {
			return null;
		}else{			
			Set<Integer> undone = new HashSet<Integer>();
			for(int i=ctx.ops.size()-1; i>=0; i--) {
				Operation op = ctx.ops.get(i);
				if(op instanceof UndoOp) {
					UndoOp undo = (UndoOp) op;
					undone.add(undo.opIndex);
					continue;
				}
				if(undone.contains(i)) {
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
	
	public void generate(int count) {
		List<CounterInfo> counters = new ArrayList<CounterInfo>(ctx.board.getCounters());
		for (int i = 0; i < count; ++i) {
			CounterInfo ci = counters.get((int) (counters.size()*Math.random()));
			int x = (int) (1 + Math.random() * 30);
			int y = (int) (1 + Math.random() * 30);
			runner.process(new Move(ci, new Hex(x,y)));
			if(i % 30 == 0) {
				runner.process(new NextPhase());
			}
		}
	} 

}
