package webboards.client.menu;

import webboards.client.data.GameCtx;
import webboards.client.display.EarlDisplay;
import webboards.client.display.EarlDisplay.Mode;
import webboards.client.ops.Operation;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;

public class HistoryControls extends Composite {
	private final Grid grid = new Grid(1, 4);
	private final GameCtx ctx;

	public HistoryControls(GameCtx context) {
		this.ctx = context;
		initWidget(grid);

		setEntries(new MenuEntry("&lt;") {
			@Override
			public void exec() {
				back();
			};
		}, new MenuEntry("&gt;") {
			@Override
			public void exec() {
				forward();
			};
		}, new MenuEntry("&gt;&gt;") {
			@Override
			public void exec() {
				fastForward();
			};
		});
	}

	private void setEntries(MenuEntry... entries) {
		int column = 0;
		for (final MenuEntry entry : entries) {
			Button button = new Button(entry.label);
			button.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					entry.exec();
				}
			});
			grid.setWidget(0, column++, button);
		}
	}

	private void back() {
		if (ctx.getPosition() > 0) {
			Operation last = ctx.ops.get(ctx.getPosition());
			last.undoUpdate(ctx.board);
			last.undoDraw(ctx);
			ctx.setPosition(ctx.getPosition() - 1);
		}
		showMode();
	}

	private void forward() {
		doForward();
		showMode();
	}

	private boolean doForward() {
		if (ctx.getPosition() < ctx.ops.size() - 1) {
			ctx.setPosition(ctx.getPosition() + 1);
			ctx.ops.get(ctx.getPosition()).updateBoard(ctx.board);
			ctx.ops.get(ctx.getPosition()).draw(ctx);
			ctx.ops.get(ctx.getPosition()).drawDetails(ctx);
			return true;
		} else {
			return false;
		}
	}

	private void showMode() {
		EarlDisplay.Mode mode = ctx.isHistoryMode() ? Mode.VIEW_ONLY : Mode.INTERACTIVE;
		ctx.display.setMode(mode);
	}

	private void fastForward() {
		while (doForward()) {
		}
		showMode();
	}
}
