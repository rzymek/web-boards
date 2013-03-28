package webboards.client.display;

import java.util.Collection;
import java.util.List;

import webboards.client.data.Board;
import webboards.client.data.CounterInfo;
import webboards.client.data.GameCtx;
import webboards.client.games.Position;

public abstract class BasicDisplay implements EarlDisplay {
	protected final GameCtx ctx;

	protected BasicDisplay(GameCtx ctx) {
		this.ctx = ctx;
		ctx.display = this;
	}

	public void setBoard(final Board board) {
		ctx.board = board;
		initAreas(board);
		initCounters(board);
	}
	
	public void updateBoard(final Board board){
		ctx.board = board;
		resetCounters();
		Collection<Position> stacks = board.getStacks();
		for (Position pos : stacks) {
			List<CounterInfo> counters = board.getInfo(pos).getPieces();
			for (CounterInfo counter : counters) {
				update(counter.ref(), counter.getState());
			}
			alignStack(pos);
		}

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
