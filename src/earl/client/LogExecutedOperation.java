package earl.client;

import earl.client.op.Operation;
import earl.client.utils.AbstractCallback;

public final class LogExecutedOperation extends AbstractCallback<Operation> {
	private final ClientEngine clientEngine;
	private final Operation op;

	public LogExecutedOperation(ClientEngine clientEngine, Operation op) {
		this.clientEngine = clientEngine;
		this.op = op;
	}

	@Override
	public void onSuccess(Operation result) {
		ClientEngine.log(op.toString());
	}
}