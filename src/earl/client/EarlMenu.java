package earl.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class EarlMenu implements ClickHandler {
	private final RootPanel root;

	public EarlMenu(final RootPanel root) {
		this.root = root;
		Button menu = add("Menu");
		add("Flip");
		add("2d6");
		add("1d6");
		add("Toggle units");
		add("Show board");
		add("End turn");
		menu.click();
	}

	private Button add(String text) {
		Button button = new Button(text);
		button.addClickHandler(this);
		root.add(button);
		return button;
	}

	@Override
	public void onClick(ClickEvent event) {
		final Button source = (Button) event.getSource();
		String text = source.getHTML();
		if("Menu".equals(text)) {
			for (int i = 1; i < root.getWidgetCount(); i++) {
				Widget w = root.getWidget(i);
				w.setVisible(!w.isVisible());
			}
		}else{
			Window.alert("TODO: "+text);
		}
	}
}
