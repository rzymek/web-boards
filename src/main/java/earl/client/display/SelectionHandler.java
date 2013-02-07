package earl.client.display;

import java.util.List;

import earl.client.data.CounterInfo;
import earl.client.data.GameCtx;
import earl.client.data.HexInfo;
import earl.client.games.Position;
import earl.client.ops.Operation;

public class SelectionHandler {
	private final GameCtx ctx;

	public SelectionHandler(GameCtx ctx) {
		this.ctx = ctx;
	}

	public void onClicked(Position position) {
		ctx.process(onPositionClicked(position));
	}

	public void onClicked(List<CounterInfo> stack, Position pos) {
		ctx.process(onStackClicked(stack, pos));
	}

	public void onClicked(CounterInfo counter) {
		ctx.process(onSingleCounterClicked(counter));
	}

	protected Operation onSingleCounterClicked(CounterInfo counter) {
		if (ctx.selected == null) {
			return counter.onSingleClicked(ctx);
		} else {
			return ctx.selected.onPointTo(ctx, counter);
		}
	}

	protected Operation onPositionClicked(Position position) {
		HexInfo area = ctx.board.getInfo(position);
		List<CounterInfo> stack = area.getPieces();
		if (stack.isEmpty()) {
			return onEmptyHexClicked(position);
		} else if (stack.size() == 1) {
			return onSingleCounterClicked(stack.iterator().next());
		} else {
			return onStackClicked(stack, position);
		}
	}

	protected Operation onEmptyHexClicked(Position empty) {
		if (ctx.selected == null) {
			return ctx.board.getInfo(empty).onEmptyClicked(ctx, empty);
		} else {
			return ctx.selected.onPointToEmpty(ctx, empty);
		}
	}

	protected Operation onStackClicked(List<CounterInfo> stack, Position pos) {
		if (ctx.selected == null) {
			return ctx.board.getInfo(pos).onStackClicked(ctx, stack, pos);
		} else {
			return ctx.selected.onPointToStack(ctx, stack, pos);
		}
	}
}
