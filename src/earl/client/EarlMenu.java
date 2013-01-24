package earl.client;

import org.vectomatic.dom.svg.impl.SVGSVGElement;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import earl.client.games.Bastogne;

public class EarlMenu implements ClickHandler {
	private final RootPanel root;
	private final SVGSVGElement svg;
	private final Bastogne game;

	public EarlMenu(final RootPanel root, SVGSVGElement svg, Bastogne game) {
		this.root = root;
		this.svg = svg;
		this.game = game;
		Button menu = add("Show menu");
		menu.setVisible(true);
		add("Flip");
		add("2d6");
		add("1d6");
		add("Toggle units");
		add("End turn");
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
		}else if("Hide menu".equals(text)){
			toggleMenu();
			source.setHTML("Show menu");
		}else if("Toggle units".equals(text)){
			toggleVisible(svg.getElementById("units").getStyle());
			toggleVisible(svg.getElementById("markers").getStyle());
		}else{
			Window.alert("TODO: "+text);
		}
	}

	private void toggleVisible(Style style) {
		if(Visibility.HIDDEN.getCssName().equals(style.getVisibility())) {
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
