package earl.client.bastogne.op;

import com.googlecode.objectify.annotation.EntitySubclass;

import earl.client.data.Board;
import earl.client.data.Counter;
import earl.client.op.EarlDisplay;
import earl.client.op.Operation;

@EntitySubclass
public class Flip extends Operation {
	Counter counter;

	@Override
	public void draw(EarlDisplay g) {
		g.update(counter, counter.getState());
	}

	@Override
	public void execute(OperationContext ctx) {
		counter.flip();
	}

	@Override
	public String encode() {
		return null;
	}

	@Override
	public void decode(Board board, String s) {
	}

}
