package webboards.client.menu;

import webboards.client.data.GameCtx;
import webboards.client.display.Display;
import webboards.client.display.Display.Mode;
import webboards.client.ops.Operation;

import com.google.gwt.user.client.ui.Composite;

public class HistoryControls extends Composite {
	private final GameCtx ctx;

	public HistoryControls(GameCtx context) {
		this.ctx = context;
		ButtonMenu menu = new ButtonMenu();
		menu.addAll(
			new MenuEntry("&lt;") {
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
			}
		);
		initWidget(menu.createHorizontal());
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
		Display.Mode mode = ctx.isHistoryMode() ? Mode.VIEW_ONLY : Mode.INTERACTIVE;
		ctx.display.setMode(mode);
	}

	private void fastForward() {
		while (doForward()) {
		}
		showMode();
	}
}
