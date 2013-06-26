package webboards.client.ops.generic;

import webboards.client.data.Board;
import webboards.client.data.GameCtx;
import webboards.client.ops.Operation;

public class UndoOp extends Operation {
	private static final long serialVersionUID = 1L;
	private Operation op;
	public Integer opIndex;

	protected UndoOp() {
	}

	public UndoOp(Operation op, Integer opIndex) {
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
