package earl.client.bastogne.op;

import earl.client.data.Board;

public interface Undoable {
	void undo(Board board);
}
