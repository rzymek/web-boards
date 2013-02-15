package webboards.client.display;

import java.util.Collection;
import java.util.List;

import webboards.client.data.Board;
import webboards.client.data.CounterInfo;
import webboards.client.data.GameCtx;
import webboards.client.games.Position;
import webboards.client.games.scs.bastogne.BastogneSide;
import webboards.client.remote.ServerEngine;
import webboards.client.remote.ServerEngineAsync;

import com.google.gwt.core.shared.GWT;

public abstract class BasicDisplay implements EarlDisplay {
	protected final ServerEngineAsync service;
	protected final GameCtx ctx = new GameCtx();

	public GameCtx getCtx() {
		return ctx;
	}
	
	protected BasicDisplay(BastogneSide side) {
		service = GWT.create(ServerEngine.class);
		ctx.display = this;
		ctx.side = side;
	}

	public void setBoard(final Board board) {
		ctx.board = board;
		initAreas(board);
		updateBoard(board);
	}
	public void updateBoard(final Board board){
		ctx.board = board;
		resetCounters();
		initCounters(board);		
	}

	protected abstract void resetCounters();

	protected void initCounters(final Board board) {
		Collection<Position> stacks = board.getStacks();
		for (Position pos : stacks) {
			List<CounterInfo> counters = board.getInfo(pos).getPieces();
			for (CounterInfo counter : counters) {
				createCounter(counter, board);
			}
			alignStack(pos);
		}
	}
	
//	protected abstract void createCounter(CounterInfo counter, Board board);

	protected abstract void initAreas(Board board);

}
