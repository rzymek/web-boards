package webboards.client.ops;

import webboards.client.data.Board;
import webboards.client.data.GameCtx;

public interface Undoable extends Operation {
	void undoUpdate(Board board);
	void undoDraw(GameCtx ctx);
}
