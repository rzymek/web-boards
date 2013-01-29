package earl.client.menu;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import earl.client.data.Counter;
import earl.client.games.scs.ops.Flip;
import earl.client.utils.TouchScrollPanel;

public class EarlMenu implements ClickHandler {
	private final VerticalPanel logPanel = new VerticalPanel();
	private final EarlClienContext ctx;
	private final RootPanel root;

	public EarlMenu(EarlClienContext ctx) {
		this.ctx = ctx;
		root = RootPanel.get("menu");
		Button menu = add("Show menu");
		menu.setVisible(true);
		add("Flip");
		add("Show log");
		add("Test log");
		add("Toggle units");
		Element log = Document.get().getElementById("log");
		log.getStyle().setVisibility(Visibility.HIDDEN);
		log.getStyle().setDisplay(Display.BLOCK);
		
		ScrollPanel scrollPanel = new TouchScrollPanel(logPanel);
		scrollPanel.setHeight("400px");
		RootPanel.get("log").add(scrollPanel);
		
		log("Playing as "+ctx.side);
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
		if("Show menu".equals(text)) {
			toggleMenu();
			source.setHTML("Hide menu");
		}else if("Show log".equals(text)){
			toggleVisible(Document.get().getElementById("log").getStyle());
		}else if("Hide menu".equals(text)){
			toggleMenu();
			source.setHTML("Show menu");
		}else if("Flip".equals(text)){
			Counter piece = ctx.handler.getSelectedPiece();
			if(piece == null) {
				Window.alert("Select a counter first");
			}
			Flip op = new Flip(piece.ref());
			ctx.display.process(op);
		}else if("Test log".equals(text)){
			for (int i = 1; i < 15; ++i) {
				log("scroll panel " + i);
			}
		}else if("Toggle units".equals(text)){
			toggleVisible(ctx.svg.getElementById("units").getStyle());
			toggleVisible(ctx.svg.getElementById("markers").getStyle());
		}else{
			Window.alert("TODO: "+text);
		}
	}

	public void log(String msg) {
		logPanel.insert(new Label(msg), 0);
	}

	private void toggleVisible(Style style) {
		String visibility = style.getVisibility();
		String hidden = Visibility.HIDDEN.getCssName();
		if(hidden.equals(visibility)) {
			style.setVisibility(Visibility.VISIBLE);
		}else{
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
