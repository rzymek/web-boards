package earl.client.ops;

import earl.client.data.Board;

public interface Undoable {
	void undo(Board board);
}
