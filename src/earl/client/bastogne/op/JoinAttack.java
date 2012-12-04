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
		Position end = g.getCenter(from);
		g.drawArrow(start, end);
	}

	@Override
	public void execute(OperationContext ctx) {
		ctx.add(target, from);
	}

	@Override
	public String encode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void decode(Board board, String s) {
		// TODO Auto-generated method stub
		
	}
}
