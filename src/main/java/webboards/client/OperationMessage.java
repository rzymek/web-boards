package webboards.client;

import no.eirikb.gwtchannelapi.client.Message;
import webboards.client.ops.Operation;

public class OperationMessage implements Message {
	private static final long serialVersionUID = 1L;
	public Operation op;

	@Override
	public String toString() {
		return "" + op;
	}
}