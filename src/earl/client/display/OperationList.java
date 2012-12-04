package earl.client.display;

import java.util.ArrayList;

import earl.client.bastogne.op.OperationContext;
import earl.client.op.EarlDisplay;
import earl.client.op.Operation;

public class OperationList extends ArrayList<Operation> {
	private final EarlDisplay display;
	private final OperationContext ctx;
	
	public OperationList(EarlDisplay display, OperationContext ctx) {
		super();
		this.display = display;
		this.ctx = ctx;
	}

	@Override
	public boolean add(Operation e) {
		boolean result = super.add(e);
		e.execute(ctx);
		e.draw(display);
		return result;
	}
}
