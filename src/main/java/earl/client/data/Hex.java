package earl.client.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import earl.client.games.HexXY;
import earl.client.games.scs.ops.Move;
import earl.client.ops.Operation;

public class Hex implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public Hex() {		
	}
	
	protected List<Counter> pieces = new ArrayList<Counter>();

	public List<Counter> getStack() {
		return Collections.unmodifiableList(pieces);
	}

	@Override
	public String toString() {
		return String.valueOf(pieces);
	}

	public Operation onClicked(GameCtx ctx, HexXY position) {
		Counter selected = ctx.selected;
		if(selected == null) {
			return null;
		}
		ctx.display.select(null);
		if(this.equals(selected.getPosition())) {
			return null;
		}else{
			return new Move(selected, position);
		}
	}
}
