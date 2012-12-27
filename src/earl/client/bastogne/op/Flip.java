package earl.client.bastogne.op;

import earl.client.data.Board;
import earl.client.data.Counter;
import earl.client.op.EarlDisplay;
import earl.client.op.Operation;

public class Flip extends Operation  implements Undoable {
	Counter counter;

	@Override
	public void draw(EarlDisplay g) {
		g.update(counter, counter.getState());
	}

	@Override
	public void clientExecute() {
		counter.flip();
	}

	@Override
	public String encode() {
		return null;
	}

	@Override
	public void decode(Board board, String s) {
	}

	@Override
	public void undo() {
		counter.flip();
	}

}
