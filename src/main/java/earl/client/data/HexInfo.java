package earl.client.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import earl.client.games.Position;
import earl.client.ops.Operation;

public class HexInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	public HexInfo() {
	}

	protected List<CounterInfo> pieces = new ArrayList<CounterInfo>();

	public List<CounterInfo> getPieces() {
		return Collections.unmodifiableList(pieces);
	}

	@Override
	public String toString() {
		return String.valueOf(pieces);
	}

	public Operation onEmptyClicked(GameCtx ctx, Position position) {
		return null;
	}

	public Operation onStackClicked(GameCtx ctx, List<CounterInfo> stack, Position pos) {
		ctx.display.showStackSelector(stack, pos);
		return null;
	}
}
