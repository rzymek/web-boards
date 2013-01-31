package earl.client.games.scs.ops;

import earl.client.data.Board;
import earl.client.data.Counter;
import earl.client.data.ref.CounterRef;
import earl.client.display.EarlDisplay;
import earl.client.ops.Operation;
import earl.client.ops.Undoable;

public class Flip extends Operation  implements Undoable {
	private static final long serialVersionUID = 1L;
	private CounterRef counterRef;
	protected Flip() {
	}
	
	public Flip(CounterRef counterRef) {
		this.counterRef = counterRef;
	}

	@Override
	public void draw(Board board, EarlDisplay g) {
		Counter counter = board.get(counterRef);
		g.update(counter, counter.getState());
	}

	@Override
	public void clientExecute(Board board) {
		Counter counter = board.get(counterRef);
		counter.flip();
	}

	@Override
	public void undo(Board board) {
		Counter counter = board.get(counterRef);
		counter.flip();
	}
	@Override
	public String toString() {
		return counterRef+" flipped.";
	}
}
