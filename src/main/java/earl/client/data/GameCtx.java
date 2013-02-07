package earl.client.data;

import com.google.gwt.core.shared.GWT;

import earl.client.ClientEngine;
import earl.client.display.EarlDisplay;
import earl.client.ops.Operation;
import earl.client.remote.ServerEngine;
import earl.client.remote.ServerEngineAsync;
import earl.client.utils.AbstractCallback;

public class GameCtx {
	public Board board;
	public EarlDisplay display;
	public CounterInfo selected;

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
