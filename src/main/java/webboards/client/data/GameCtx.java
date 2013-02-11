package webboards.client.data;

import webboards.client.ClientEngine;
import webboards.client.display.EarlDisplay;
import webboards.client.games.scs.bastogne.BastogneSide;
import webboards.client.ops.Operation;
import webboards.client.remote.ServerEngine;
import webboards.client.remote.ServerEngineAsync;
import webboards.client.utils.AbstractCallback;

import com.google.gwt.core.shared.GWT;

public class GameCtx {
	public Board board;
	public EarlDisplay display;
	public CounterInfo selected;
	public BastogneSide side;
	
	public GameCtx() {		
	}
	
	public void process(Operation op) {
		if (op == null) {
			return;
		}
		op.updateBoard(board);
		op.draw(this);
		ServerEngineAsync service = GWT.create(ServerEngine.class);
		service.process(op, new AbstractCallback<Operation>() {
			@Override
			public void onSuccess(Operation result) {
				result.postServer(GameCtx.this);
				result.drawDetails(GameCtx.this);
				ClientEngine.log(""+result);
			}
		});
	}
}
