package webboards.client.games;

import webboards.client.games.scs.bastogne.BastogneSide;

public class TurnPhase {

	private final BastogneSide side;
	private final Phase phase;

	public TurnPhase(BastogneSide side, Phase phase) {
		this.side = side;
		this.phase = phase;
	}

}
