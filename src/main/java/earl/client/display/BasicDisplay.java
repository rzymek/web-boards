package earl.client.display;

import java.util.Collection;
import java.util.List;

import com.google.gwt.core.shared.GWT;

import earl.client.ClientEngine;
import earl.client.data.Board;
import earl.client.data.CounterInfo;
import earl.client.data.GameCtx;
import earl.client.games.Position;
import earl.client.remote.ServerEngine;
import earl.client.remote.ServerEngineAsync;

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
			List<CounterInfo> counters = board.getInfo(pos).getPieces();
			ClientEngine.log(pos + ": " + counters);
			for (CounterInfo counter : counters) {
				createCounter(counter, board);
			}
			alignStack(pos);
		}
	}
	
	protected abstract void createCounter(CounterInfo counter, Board board);

	protected abstract void initAreas(Board board);

}
