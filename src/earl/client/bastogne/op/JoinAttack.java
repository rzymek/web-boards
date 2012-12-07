package earl.client.bastogne.op;

import com.googlecode.objectify.annotation.EntitySubclass;

import earl.client.data.Board;
import earl.client.data.Hex;
import earl.client.op.EarlDisplay;
import earl.client.op.Operation;
import earl.client.op.Position;

@EntitySubclass
public class JoinAttack extends Operation {
	public Hex from;
	public Hex target;

	@Override
	public void draw(EarlDisplay g) {
		Position start = g.getCenter(from);
		Position end = g.getCenter(target	);
		g.drawArrow(start, end, from.getId());
	}

	@Override
	public void execute(OperationContext ctx) {
//		ctx.add(target, from);
	}

	@Override
	public String encode() {
		return encode(from, target);
	}

	@Override
	public void decode(Board board, String s) {
		String[] data = s.split(":");
		from = board.getHex(data[0]);
		target = board.getHex(data[1]);
	}
	
	@Override
	public String toString() {
		return "Declared attack from "+from.getId()+" to "+target.getId();
	}
	
}
