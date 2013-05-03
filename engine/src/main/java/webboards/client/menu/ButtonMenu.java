package webboards.client.menu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;

public class ButtonMenu {
	public static enum Layout {
		HORIZ, VERT
	}

	public ButtonMenu(MenuEntry... entries) {
		addAll(entries);
	}

	private List<Button> buttons = new ArrayList<Button>();

	public void add(final MenuEntry entry) {
		Button button = new Button(entry.label);
		button.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				entry.exec();
			}
		});
		buttons.add(button);
	}

	public void addAll(MenuEntry... entries) {
		addAll(Arrays.asList(entries));
	}

	public void addAll(List<MenuEntry> entries) {
		for (MenuEntry entry : entries) {
			add(entry);
		}
	}

	public Grid createHorizontal() {
		Grid grid = new Grid(1, buttons.size());
		for (int column = 0; column < buttons.size(); ++column) {
			grid.setWidget(0, column, buttons.get(column));
		}
		return grid;
	}

	public Grid createVertical() {
		Grid grid = new Grid(buttons.size(), 1);
		for (int row = 0; row < buttons.size(); ++row) {
			grid.setWidget(row, 0, buttons.get(row));
		}
		return grid;
	}
}
