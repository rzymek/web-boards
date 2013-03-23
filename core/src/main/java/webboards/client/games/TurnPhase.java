package webboards.client.games;

import webboards.client.games.scs.bastogne.BastogneSide;

public class TurnPhase {
	public final BastogneSide side;
	public final Phase phase;

	public TurnPhase(BastogneSide side, Phase phase) {
		this.side = side;
		this.phase = phase;
	}
	
	@Override
	public String toString() {
		return side + " " + phase;
	}

}
