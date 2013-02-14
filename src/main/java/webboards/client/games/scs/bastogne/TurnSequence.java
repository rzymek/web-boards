package webboards.client.games.scs.bastogne;

import webboards.client.games.TurnPhase;
import webboards.client.games.scs.SCSPhase;

public class TurnSequence {
	public final static TurnPhase[] PHASES = {
		new TurnPhase(BastogneSide.US, SCSPhase.Reinforcements),
		new TurnPhase(BastogneSide.US, BastognePhase.Road_March),
		new TurnPhase(BastogneSide.US, BastognePhase.Air_Barrage),
		new TurnPhase(BastogneSide.US, SCSPhase.Movement),
		new TurnPhase(BastogneSide.US, SCSPhase.Combat),
		new TurnPhase(BastogneSide.US, SCSPhase.Exploitation),
		
		new TurnPhase(BastogneSide.GE, SCSPhase.Reinforcements),
		new TurnPhase(BastogneSide.GE, BastognePhase.Road_March),
		new TurnPhase(BastogneSide.GE, SCSPhase.Movement),
		new TurnPhase(BastogneSide.GE, BastognePhase.DG_Removal),
		new TurnPhase(BastogneSide.US, BastognePhase.Barrage),
		new TurnPhase(BastogneSide.GE, BastognePhase.Barrage),
		new TurnPhase(BastogneSide.GE, SCSPhase.Combat),
		new TurnPhase(BastogneSide.GE, SCSPhase.Exploitation),
	};
}
