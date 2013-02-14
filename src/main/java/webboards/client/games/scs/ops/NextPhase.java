package webboards.client.games.scs.ops;

import webboards.client.data.Board;
import webboards.client.data.GameCtx;
import webboards.client.games.TurnPhase;
import webboards.client.games.scs.SCSBoard;
import webboards.client.games.scs.bastogne.TurnSequence;
import webboards.client.ops.AbstractOperation;
import webboards.client.ops.Undoable;

public class NextPhase extends AbstractOperation implements Undoable {
	private static final long serialVersionUID = 1L;
	private String desc = "";

	@Override
	public void updateBoard(Board b) {
		SCSBoard board = (SCSBoard) b;
		desc = TurnSequence.PHASES[board.phase].toString();
		board.phase++;
		if (board.phase >= TurnSequence.PHASES.length) {
			board.turn++;
			board.phase = 0;
		}
		desc += " -> " + TurnSequence.PHASES[board.phase];
	}
	@Override
	public void draw(GameCtx ctx) {
		SCSBoard board = (SCSBoard) ctx.board;
		TurnPhase current = TurnSequence.PHASES[board.phase];
		String phaseText = (board.phase+1)+": "+current;
		ctx.display.setText("phaseText", phaseText);
		ctx.display.setText("turnText", "Turn: "+(board.turn+1));		
	}
	
	@Override
	public void undoUpdate(Board b) {
		SCSBoard board = (SCSBoard) b;
		if(board.turn <= 0 && board.phase <= 0) {
			return;
		}
		board.phase--;
		if (board.phase < 0) {
			board.turn--;
			board.phase = TurnSequence.PHASES.length-1;
		}
	}
	
	@Override
	public void undoDraw(GameCtx ctx) {
		draw(ctx);
	}

	@Override
	public String toString() {
		return desc;
	}

}
