package earl.client.bastogne.op;

import earl.client.op.Operation;

public class ChatMessage extends Operation {
	private static final long serialVersionUID = 1L;
	
	public String text;

	@Override
	public String toString() {
		return "> "+text;
	}	
}
