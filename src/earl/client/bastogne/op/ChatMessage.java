package earl.client.bastogne.op;

import earl.client.data.Board;
import earl.client.op.Operation;

public class ChatMessage extends Operation {

	public String text;

	@Override
	public String encode() {
		return text;
	}

	@Override
	public void decode(Board board, String s) {
		this.text = s;
	}

	@Override
	public String toString() {
		return "> "+text;
	}	
}
