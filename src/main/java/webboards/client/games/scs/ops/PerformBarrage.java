package webboards.client.games.scs.ops;

import webboards.client.data.GameCtx;
import webboards.client.data.ref.CounterId;
import webboards.client.games.Hex;
import webboards.client.games.scs.SCSCounter;
import webboards.client.games.scs.SCSMarker;
import webboards.client.games.scs.bastogne.BastogneSide;
import webboards.client.ops.Operation;

public class PerformBarrage extends Operation {
	private static final long serialVersionUID = 1L;
	private CounterId arty;
	private Hex target;

	@SuppressWarnings("unused")
	private PerformBarrage() {
	}

	public PerformBarrage(SCSCounter arty, Hex target) {
		this.arty = arty.ref();
		this.target = target;
	}

	@Override
	public void postServer(GameCtx ctx) {
		SCSMarker dg = new SCSMarker("dg"+target, "admin/misc_us-dg.png", BastogneSide.US);
		ctx.board.place(target, dg);
		ctx.display.createCounter(dg, ctx.board);
		ctx.display.alignStack(target);
		
		ctx.display.clearOds(arty.toString());
		ctx.display.showResults(ctx.display.getCenter(target), "Hit");
	}

	@Override
	public String toString() {
		return "Barrage on " + target + " by " + arty;
	}

}
