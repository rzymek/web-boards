package earl.client.display;

import java.util.Collection;
import java.util.List;

import com.google.gwt.core.shared.GWT;

import earl.client.ClientEngine;
import earl.client.data.Board;
import earl.client.data.CounterInfo;
import earl.client.data.GameCtx;
import earl.client.games.Position;
import earl.client.ops.Operation;
import earl.client.remote.ServerEngine;
import earl.client.remote.ServerEngineAsync;
import earl.client.utils.AbstractCallback;

public abstract class BasicDisplay implements EarlDisplay {
	protected final ServerEngineAsync service;
	protected final GameCtx ctx = new GameCtx();

	public GameCtx getCtx() {
		return ctx;
	}
	
	protected BasicDisplay() {
		service = GWT.create(ServerEngine.class);
		ctx.display = this;
	}

	public void setBoard(final Board board) {
		ctx.board = board;
		initAreas(board);
		initCounters(board);
	}

	protected void initCounters(final Board board) {
		Collection<Position> stacks = board.getStacks();
		ClientEngine.log("counters: " + board.getCounters().toString());
		ClientEngine.log("stacks: " + stacks.toString());
		for (Position pos : stacks) {
			List<CounterInfo> counters = board.getInfo(pos).getStack();
			ClientEngine.log(pos + ": " + counters);
			for (CounterInfo counter : counters) {
				createCounter(counter, board);
			}
			alignStack(pos);
		}
	}

	public void process(final Operation op) {
		if (op == null) {
			return;
		}
		op.updateBoard(ctx.board);
		op.draw(ctx);
		op.drawDetails(this);
		service.process(op, new AbstractCallback<Operation>() {
			@Override
			public void onSuccess(Operation result) {
				result.postServer(ctx);
				ClientEngine.log("executed: " + result);
			}
		});
	}

	protected abstract void createCounter(CounterInfo counter, Board board);

	protected abstract void initAreas(Board board);

}
