package webboards.client.data;

import java.util.ArrayList;

import webboards.client.ClientOpRunner;
import webboards.client.display.EarlDisplay;
import webboards.client.ops.Operation;

public class GameCtx {
	public Board board;
	public EarlDisplay display;
	public CounterInfo selected;
	public Side side;
	public GameInfo info;
	public ArrayList<Operation> ops;
	private ClientOpRunner runner = new ClientOpRunner(this);

	public void setInfo(GameInfo info) {
		this.side = info.side;
		this.ops = new ArrayList<Operation>(info.ops);
		this.info = info;
	}

	public void process(Operation op) {
		runner.process(op);
	}
}
