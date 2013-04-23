package webboards.client.menu;

import webboards.client.data.GameCtx;
import webboards.client.ops.Operation;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;

public class HistoryControls extends Composite {
	private Grid grid = new Grid(1, 4);
	private GameCtx ctx;

	public HistoryControls(GameCtx context) {
		this.ctx = context;
		initWidget(grid);

		setEntries(new MenuEntry("&lt;") {
			public void exec() {
				back();
			};
		}, new MenuEntry("[]") {
			public void exec() {
				reset();
			};
		}, new MenuEntry("&gt;") {
			public void exec() {
				forward();
			};
		}, new MenuEntry("&gt;&gt;") {
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
		if (ctx.position > 0) {
			Operation last = ctx.ops.get(ctx.position);
			last.undoUpdate(ctx.board);
			last.undoDraw(ctx);
			ctx.position--;
		}
	}

	private void reset() {
		while (ctx.position < ctx.ops.size()) {
			ctx.ops.remove(ctx.ops.size() - 1);
		}
		ctx.position--;
	}

	private boolean forward() {
		if (ctx.position < ctx.ops.size() - 1) {
			ctx.ops.get(ctx.position).updateBoard(ctx.board);
			ctx.ops.get(ctx.position).draw(ctx);
			ctx.ops.get(ctx.position).drawDetails(ctx);
			ctx.position++;
			return true;
		} else {
			return false;
		}
	}

	private void fastForward() {
		while (forward()) {
		}
	}
}
