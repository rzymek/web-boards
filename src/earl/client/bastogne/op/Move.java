package earl.client.bastogne.op;

import com.googlecode.objectify.annotation.EntitySubclass;

import earl.client.data.Board;
import earl.client.data.Counter;
import earl.client.data.Hex;
import earl.client.op.EarlDisplay;
import earl.client.op.Operation;

@EntitySubclass
public class Move extends Operation {
	public Counter counter;
	public Hex from;
	public Hex to;

	@Override
	public void execute(OperationContext ctx) {
		counter.setPosition(to);
	}

	@Override
	public void draw(EarlDisplay g) {
		g.alignStack(from);
		g.alignStack(to);
	}
	
	@Override
	public String encode() {
		return encode(counter, from, to);
	}
	@Override
	public void decode(Board board, String s) {
		String[] data = s.split(":");
		counter = board.getCounter(data[0]);
		from = board.getHex(data[1]);
		to = board.getHex(data[2]);
	}
	@Override
	public String toString() {
		return counter+" moves from "+from.getId()+" to "+to.getId();
	}
}
