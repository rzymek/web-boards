package earl.client.display.svg;

import java.util.Collection;
import java.util.List;

import com.google.gwt.core.shared.GWT;

import earl.client.data.Board;
import earl.client.data.Counter;
import earl.client.data.Hex;
import earl.client.op.EarlDisplay;
import earl.client.op.Operation;
import earl.client.remote.ServerEngine;
import earl.client.remote.ServerEngineAsync;
import earl.client.utils.AbstractCallback;

public abstract class BasicDisplay implements EarlDisplay {

	private final ServerEngineAsync service;

	protected BasicDisplay() {
		service = GWT.create(ServerEngine.class);
	}
	
	public void setBoard(final Board board) {
		initAreas(board);
		Collection<Hex> stacks = board.getStacks();
		for (Hex hex : stacks) {
			List<Counter> counters = hex.getStack();
			for (Counter counter : counters) {
				createCounter(counter, board);
			}
			alignStack(hex);
		}
	}

	protected void process(Operation op) {
		if (op == null) {
			return;
		}
		op.execute(null);
		op.draw(this);
		service.process(op, new AbstractCallback<Void>());
	}

	protected abstract void createCounter(Counter counter, Board board);

	protected abstract void initAreas(Board board);

}
