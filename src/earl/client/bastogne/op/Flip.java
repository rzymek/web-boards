package earl.client.bastogne.op;

import earl.client.data.Board;
import earl.client.data.Counter;
import earl.client.op.EarlDisplay;
import earl.client.op.Operation;

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
}
