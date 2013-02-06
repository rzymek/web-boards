package earl.client.games.scs.ops;

import earl.client.data.Board;
import earl.client.data.Counter;
import earl.client.data.ref.CounterRef;
import earl.client.display.EarlDisplay;
import earl.client.games.Ref;
import earl.client.ops.Operation;

public class Move extends Operation {
	private static final long serialVersionUID = 1L;
	public CounterRef counterRef;
	public Ref fromRef;
	public Ref toRef;

	protected Move() {		
	}
	
	public Move(Counter counter, Ref to) {
		counterRef = counter.ref();
		Ref from = counter.getPosition();
		if(from != null) {
			fromRef = from;
		}
		toRef = to;
	}

	@Override
	public void updateBoard(Board board) {
		Counter counter = board.get(counterRef);
		board.place(toRef, counter);
	}

	@Override
	public void draw(Board board, EarlDisplay g) {
		g.alignStack(fromRef);
		g.alignStack(toRef);
	}
	
	@Override
	public void drawDetails(EarlDisplay g) {
		g.drawLine(fromRef, toRef);
	}
	
	@Override
	public String toString() {
		return counterRef+" moves from "+fromRef+" to "+toRef;
	}
}
