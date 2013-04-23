package webboards.client.data;

import java.util.ArrayList;

import webboards.client.display.EarlDisplay;
import webboards.client.ops.Operation;

public class GameCtx {
	public Board board;
	public EarlDisplay display;
	public CounterInfo selected;
	public Side side;
	public GameInfo info;
	public ArrayList<Operation> ops;
	public int position;

	public void setInfo(GameInfo info) {
		this.side = info.side;
		this.ops = new ArrayList<Operation>(info.ops);
		this.info = info;
		this.position = ops.size() - 1;
	}

	public boolean isHistoryMode() {
		return position != ops.size() - 1;
	}
}
