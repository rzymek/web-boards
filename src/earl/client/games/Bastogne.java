package earl.client.games;

import java.util.Collection;

import earl.client.data.Board;
import earl.client.data.Counter;
import earl.client.data.Hex;

public class Bastogne implements Game {
	private final Board board;
	private String playerUS;
	private String playerGE;

	public Bastogne(){
		this.board = new Board();		
	}
	
	private void setup(String hexId, String counterFront, String desc) {
		String id = counterFront.substring(counterFront.indexOf("+")+1);
		id = id.substring(0, id.indexOf("_f."));
		SCSCounter counter = new SCSCounter(id, counterFront);
		counter.setDescription(desc);
		board.place(hexId, counter);
	}
	public static void main(String[] args) {
		Bastogne b = new Bastogne();
		b.setupScenarion52();
		Collection<Hex> stacks = b.getBoard().getStacks();
		for (Hex hex : stacks) {
			for (Counter c : hex.getStack()) {
				System.out.println(c.getId());				
			}
		}
		System.out.println("Bastogne.main()");
	}
	public void setupScenarion52() {
		setup("50.21", "1-3-6+SNAFU_AdHoc_f.png", "Ad Hoc Inf Co (SNAFU)");
		setup("50.22", "2-3-12+CCR_C-482_f.png", "CCR (C/482 Arm AA Co)");
		setup("47.22", "2_8-1-16+CCR_HHC_f.png", "CCR (HHC Arm Arty Platoon)");
		setup("37.17", "3-3-16+Cherry_D-90_f.png", "Team Cherry (D/90 Arm Recon Platoon)");
		setup("37.17", "2_8-1-16+CCR_HHC_f.png", "Team Cherry (HHC/3 Mortar Platoon)");
		setup("49.22", "4_14-1-12+58_Art_f.png", "58 Arty Bn");
		setup("49.24", "2-3-14+Cherry_C-609_f.png", "Team Cherry (C/609 TD Platoon)");
		setup("49.23", "5-3-12+Cherry_A-3_f.png", "Team Cherry (A/3 Arm Co)");
		setup("48.23", "4_14-1-12+73_Art_f.png", "73 Arm Arty Bn");
		setup("46.22", "4-5-14+Cherry_C-20_f.png", "Team Cherry (C/20 Arm Inf Co)");
		setup("45.22", "3-2-14+Cherry_D-3_f.png", "Team Cherry (D/3 Arm Platoon)");
		setup("37.20", "0-1-6+Comb_C_f.png", "Comb Eng Bn (C Co)");
		setup("40.19", "0-1-6+Comb_D_f.png", "Comb Eng Bn (D Co)");

		setup("33.16-21", "2-4-6+501_A-1_f.png", "1st Bn 501 Abn Inf (A/1)");
		setup("33.16-21", "2-4-6+501_B-1_f.png", "1st Bn 501 Abn Inf (B/1)");
		setup("33.16-21", "2-4-6+501_C-1_f.png", "1st Bn 501 Abn Inf (C/1)");
		setup("33.16-21", "2-4-6+501_D-2_f.png", "2nd Bn 501 Abn Inf (D/2)");
		setup("33.16-21", "2-4-6+501_E-2_f.png", "2nd Bn 501 Abn Inf (E/2)");
		setup("33.16-21", "2-4-6+501_F-2_f.png", "2nd Bn 501 Abn Inf (F/2)");
		setup("33.16-21", "2-4-6+501_G-3_f.png", "3rd Bn 501 Abn Inf (G/3)");
		setup("33.16-21", "2-4-6+501_H-3_f.png", "3rd Bn 501 Abn Inf (H/3)");
		setup("33.16-21", "4_8-1-12+101_327_f.png", "377 Abn Arty Bn");
	}

	public void setId(String string) {
		
	}

	public void setPlayer(BastogneSide side, String player) {
		if(side == BastogneSide.US) {
			this.playerUS = player;
		}else{
			this.playerGE = player;
		}
	}

	public String getPlayer(BastogneSide side) {
		if(side == BastogneSide.US) {
			return playerUS;
		}else{
			return playerGE;
		}
	}

	@Override
	public Board getBoard() {
		return board;
	}
	
	@Override
	public boolean isParticipating(String user) {
		return user.equals(playerUS) || user.equals(playerGE);
	}

}
