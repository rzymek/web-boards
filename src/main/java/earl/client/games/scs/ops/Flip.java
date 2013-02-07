package earl.client.games.scs.ops;

import earl.client.data.Board;
import earl.client.data.CounterInfo;
import earl.client.data.GameCtx;
import earl.client.data.ref.Counter;
import earl.client.ops.Operation;

public class Flip extends Operation {
	private static final long serialVersionUID = 1L;
	private Counter counterRef;
	protected Flip() {
	}
	
	public Flip(Counter counterRef) {
		this.counterRef = counterRef;
	}

	@Override
	public void draw(GameCtx ctx) {
		CounterInfo counter = ctx.board.getInfo(counterRef);
		ctx.display.update(counter, counter.getState());
	}

	@Override
	public void updateBoard(Board board) {
		CounterInfo counter = board.getInfo(counterRef);
		counter.flip();
	}

	@Override
	public String toString() {
		return counterRef+" flipped.";
	}
}
