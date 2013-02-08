package webboards.client.data;

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
	public GameCtx() {		
	}
	
	public void process(final Operation op) {
		if (op == null) {
			return;
		}
		op.updateBoard(board);
		op.draw(this);
		op.drawDetails(display);
		ServerEngineAsync service = GWT.create(ServerEngine.class);
		service.process(op, new AbstractCallback<Operation>() {
			@Override
			public void onSuccess(Operation result) {
				result.postServer(GameCtx.this);
				ClientEngine.log("executed: " + result);
			}
		});
	}
}