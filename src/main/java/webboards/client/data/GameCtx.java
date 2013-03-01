package webboards.client.data;

import java.util.ArrayList;
import java.util.List;

import webboards.client.ClientEngine;
import webboards.client.display.EarlDisplay;
import webboards.client.ops.Operation;
import webboards.client.remote.ServerEngine;
import webboards.client.remote.ServerEngineAsync;
import webboards.client.utils.AbstractCallback;

import com.google.gwt.core.shared.GWT;

public class GameCtx {
	public Board board;
	public EarlDisplay display;
	public CounterInfo selected;
	public Side side;
	public GameInfo info;
	public List<Operation> ops;
	private final ServerEngineAsync service = GWT.create(ServerEngine.class);

	public GameCtx() {
	}

	public void process(Operation op) {
		if (op == null) {
			return;
		}
		op.updateBoard(board);
		op.draw(this);
		ops.add(op);
		queue.add(op);
		processQueued();
	}

	//see: https://gist.github.com/chumpy/1696249
	private final List<Operation> queue = new ArrayList<Operation>();
	private boolean processing = false;
	private void processQueued() {
		if (processing || queue.isEmpty()) {
			return;
		}
		processing = true;
		Operation op = queue.remove(queue.size() - 1);
		service.process(op, new AbstractCallback<Operation>() {
			@Override
			public void onSuccess(Operation result) {
				try {
					processing = false;
					result.postServer(GameCtx.this);
					result.drawDetails(GameCtx.this);
					ClientEngine.log("" + result);
				} finally {
					processQueued();
				}
			}
		});
	}
}
