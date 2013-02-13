package webboards.client.ops.generic;

import webboards.client.data.Board;
import webboards.client.data.GameCtx;
import webboards.client.ops.AbstractOperation;
import webboards.client.ops.Undoable;

public class UndoOp extends AbstractOperation implements Undoable {
	private static final long serialVersionUID = 1L;
	private Undoable op;

	@SuppressWarnings("unused")
	private UndoOp() {
	}

	public UndoOp(Undoable op) {
		this.op = op;
	}

	@Override
	public void updateBoard(Board board) {
		op.undoUpdate(board);
	}

	@Override
	public void draw(GameCtx ctx) {
		op.undoDraw(ctx);
	}

	@Override
	public String toString() {
		return "Undo " + op;
	}
}
