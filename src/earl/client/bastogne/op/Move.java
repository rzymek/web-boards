package earl.client.bastogne.op;

import earl.client.data.Board;
import earl.client.data.Counter;
import earl.client.data.Hex;
import earl.client.op.EarlDisplay;
import earl.client.op.Operation;

public class Move extends Operation implements Undoable {
	private static final long serialVersionUID = 1L;
	public CounterRef counterRef;
	public HexRef fromRef;
	public HexRef toRef;

	@Override
	public void clientExecute(Board board) {
		Counter counter = board.get(counterRef);
		Hex to = board.get(toRef);
		counter.setPosition(to);
	}

	@Override
	public void draw(Board board, EarlDisplay g) {
		Hex from = board.get(fromRef);
		Hex to = board.get(toRef);
		g.alignStack(from);
		g.alignStack(to);
	}
	
	@Override
	public String toString() {
		return counterRef+" moves from "+fromRef.getId()+" to "+toRef.getId();
	}

	@Override
	public void undo(Board board) {
		Counter counter = board.get(counterRef);
		Hex from = board.get(fromRef);
		counter.setPosition(from);
	}

}
