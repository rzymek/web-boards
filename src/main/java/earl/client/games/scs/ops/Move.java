package earl.client.games.scs.ops;

import earl.client.data.Board;
import earl.client.data.Counter;
import earl.client.data.Hex;
import earl.client.data.ref.CounterRef;
import earl.client.data.ref.HexRef;
import earl.client.display.EarlDisplay;
import earl.client.ops.Operation;
import earl.client.ops.Undoable;

public class Move extends Operation implements Undoable {
	private static final long serialVersionUID = 1L;
	public CounterRef counterRef;
	public HexRef fromRef;
	public HexRef toRef;

	protected Move() {		
	}
	
	public Move(Counter counter, Hex to) {
		counterRef = counter.ref();
		Hex from = counter.getPosition();
		if(from != null) {
			fromRef = from.ref();
		}
		toRef = to.ref();
	}

	@Override
	public void updateBoard(Board board) {
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
	public void drawDetails(EarlDisplay g) {
		g.drawLine(fromRef, toRef);
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
