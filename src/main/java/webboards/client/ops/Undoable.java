package webboards.client.ops;

import webboards.client.data.GameCtx;

public interface Undoable {

	void undo(GameCtx ctx);

}
