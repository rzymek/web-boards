package webboards.client.data;

import java.util.ArrayList;
import java.util.List;

import webboards.client.ClientEngine;
import webboards.client.ex.ConcurrentOpException;
import webboards.client.ops.Operation;
import webboards.client.remote.ServerEngine;
import webboards.client.remote.ServerEngineAsync;
import webboards.client.utils.AbstractCallback;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Window;

public class ClientOpRunner extends AbstractCallback<Operation> {
	public final static ServerEngineAsync service = GWT.create(ServerEngine.class);
	private final GameCtx ctx;
	
	public ClientOpRunner(GameCtx ctx) {
		super();
		this.ctx = ctx;
	}

	public void process(Operation op) {
		if (op == null) {
			return;
		}
		op.index = ctx.ops.size();	
		preServerExec(op);
		ctx.ops.add(op);
		queue.add(op);
		processQueued();
	}

	private void preServerExec(Operation op) {
		op.updateBoard(ctx.board);
		op.draw(ctx);
	}
	
	private void postServerExec(Operation result) {
		result.postServer(ctx);
		result.drawDetails(ctx);
		ClientEngine.log("" + result);
	}
	
	private final List<Operation> queue = new ArrayList<Operation>();
	private boolean processing = false;
	
	/** see: https://gist.github.com/chumpy/1696249 */
	private void processQueued() {
		try {
			if (processing || queue.isEmpty()) {
				return;
			}
			processing = true;
			Operation op = queue.remove(queue.size() - 1);
			service.process(op, this);
		} catch (ConcurrentOpException e) {
			//TODO
			Window.alert(e.toString());
		}
	}
	
	@Override
	public void onSuccess(Operation result) {
		try {
			processing = false;
			postServerExec(result);
		} finally {
			processQueued();
		}
	}

}
