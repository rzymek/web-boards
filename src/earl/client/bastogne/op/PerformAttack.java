package earl.client.bastogne.op;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.googlecode.objectify.annotation.EntitySubclass;

import earl.client.data.Board;
import earl.client.data.Counter;
import earl.client.data.Hex;
import earl.client.games.SCSCounter;
import earl.client.op.EarlDisplay;
import earl.client.op.Operation;

@EntitySubclass
public class PerformAttack extends Operation {
	public Hex target;

	@Override
	public void draw(EarlDisplay g) {
	}

	
	@Override
	public void execute(OperationContext ctx) {
		List<SCSCounter> attacking = getAttackUnits(ctx);
		// TODO
	}

	@Override
	public boolean isValid(OperationContext ctx) {
		return !ctx.get(target).isEmpty();
	}
	
	protected List<SCSCounter> getAttackUnits(OperationContext ctx) {
		@SuppressWarnings("unchecked")
		Collection<Hex> from = (Collection<Hex>) ((Collection<?>) ctx.get(target));
		List<SCSCounter> attacking = new ArrayList<SCSCounter>();
		for (Hex hex : from) {
			List<Counter> stack = hex.getStack();
			for (Counter counter : stack) {
				if (counter instanceof SCSCounter) {
					attacking.add((SCSCounter) counter);
				}
			}
		}
		return attacking;
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
