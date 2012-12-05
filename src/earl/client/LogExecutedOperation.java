package earl.client;

import earl.client.op.Operation;
import earl.client.utils.AbstractCallback;

final class LogExecutedOperation extends AbstractCallback<String> {
	/**
	 * 
	 */
	private final ClientEngine clientEngine;
	private final Operation op;

	LogExecutedOperation(ClientEngine clientEngine, Operation op) {
		this.clientEngine = clientEngine;
		this.op = op;
	}

	@Override
	public void onSuccess(String result) {
		op.decode(this.clientEngine.board, result);
		ClientEngine.log(op.toString());
	}
}