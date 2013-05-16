package webboards.client.data;

import java.util.ArrayList;

import webboards.client.display.Display;
import webboards.client.ops.Operation;

public class GameCtx {
	public Board board;
	public Display display;
	public CounterInfo selected;
	public Side side;
	public GameInfo info;
	public ArrayList<Operation> ops;
	private int position = -1;

	public GameCtx() {
	}

	public void setInfo(GameInfo info) {
		this.side = info.side;
		this.ops = new ArrayList<Operation>(info.ops);
		this.info = info;
		this.setPosition(ops.size() - 1);
	}

	public boolean isHistoryMode() {
		return getPosition() != ops.size() - 1;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}
}
