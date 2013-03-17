package webboards.client.ops;

import webboards.client.data.Board;
import webboards.client.data.GameCtx;

public interface Undoable {
	void undoUpdate(Board board);
	void undoDraw(GameCtx ctx);
}
