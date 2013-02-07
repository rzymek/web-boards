package earl.client.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import earl.client.games.Hex;
import earl.client.games.scs.ops.Move;
import earl.client.ops.Operation;

public class HexInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	public HexInfo() {
	}

	protected List<CounterInfo> pieces = new ArrayList<CounterInfo>();

	public List<CounterInfo> getStack() {
		return Collections.unmodifiableList(pieces);
	}

	@Override
	public String toString() {
		return String.valueOf(pieces);
	}

	public Operation onClicked(GameCtx ctx, Hex position) {
		CounterInfo selected = ctx.selected;
		if (selected == null) {
			return null;
		}
		ctx.display.select(null);
		if (this.equals(selected.getPosition())) {
			return null;
		} else {
			return new Move(selected, position);
		}
	}
}
