package webboards.client.ops.generic;

import webboards.client.data.Board;
import webboards.client.data.GameCtx;
import webboards.client.ops.Operation;
import webboards.client.ops.Undoable;

public class UndoOp extends Operation implements Undoable {
	private static final long serialVersionUID = 1L;
	private Undoable op;
	public Integer opIndex;

	@SuppressWarnings("unused")
	private UndoOp() {
	}
 
	public UndoOp(Undoable op, Integer opIndex) {
		this.op = op;
		this.opIndex = opIndex;
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
