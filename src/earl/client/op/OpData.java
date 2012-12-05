package earl.client.op;

import java.io.Serializable;

import earl.client.data.Board;

public class OpData implements Serializable {
	private String data = null;
	private Operation instance = null;

	protected OpData() {
	}
	
	public OpData(String data, Operation instance) {
		super();
		this.data = data;
		this.instance = instance;
	}
	
	public Operation get(Board board) {
		instance.decode(board, data);
		return instance;
	}

}
