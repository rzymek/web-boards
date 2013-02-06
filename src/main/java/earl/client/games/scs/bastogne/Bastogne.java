package earl.client.games.scs.bastogne;

import java.util.Collection;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

import earl.client.data.Board;
import earl.client.data.Counter;
import earl.client.data.Game;
import earl.client.data.Hex;
import earl.client.display.svg.SVGDisplay;
import earl.client.ex.EarlException;
import earl.client.games.AreaRef;
import earl.client.games.scs.CombatResult;
import earl.client.games.scs.SCSBoard;
import earl.client.games.scs.SCSCounter;
import earl.client.games.scs.SCSHex;
import earl.client.games.scs.SCSMarker;
import earl.client.ops.Operation;
import static earl.client.games.scs.bastogne.BastogneUnits.ge_26VG_1_I_77;
import static earl.client.games.scs.bastogne.BastogneUnits.ge_26VG_1_I_78;
import static earl.client.games.scs.bastogne.BastogneUnits.ge_26VG_2_I_77;
import static earl.client.games.scs.bastogne.BastogneUnits.ge_26VG_2_I_78;
import static earl.client.games.scs.bastogne.BastogneUnits.ge_26VG_3_I_77;
import static earl.client.games.scs.bastogne.BastogneUnits.ge_26VG_3_I_78;
import static earl.client.games.scs.bastogne.BastogneUnits.ge_26VG_4_I_77;
import static earl.client.games.scs.bastogne.BastogneUnits.ge_26VG_4_I_78;
import static earl.client.games.scs.bastogne.BastogneUnits.ge_26VG_5_II_77;
import static earl.client.games.scs.bastogne.BastogneUnits.ge_26VG_5_II_78;
import static earl.client.games.scs.bastogne.BastogneUnits.ge_26VG_6_II_77;
import static earl.client.games.scs.bastogne.BastogneUnits.ge_26VG_6_II_78;
import static earl.client.games.scs.bastogne.BastogneUnits.ge_26VG_7_II_77;
import static earl.client.games.scs.bastogne.BastogneUnits.ge_26VG_7_II_78;
import static earl.client.games.scs.bastogne.BastogneUnits.ge_26VG_8_II_77;
import static earl.client.games.scs.bastogne.BastogneUnits.ge_26VG_8_II_78;
import static earl.client.games.scs.bastogne.BastogneUnits.ge_26VG_IV_26;
import static earl.client.games.scs.bastogne.BastogneUnits.ge_26VG_S_II_77;
import static earl.client.games.scs.bastogne.BastogneUnits.ge_26VG_S_II_78;
import static earl.client.games.scs.bastogne.BastogneUnits.ge_26VG_S_I_77;
import static earl.client.games.scs.bastogne.BastogneUnits.ge_26VG_S_I_78;
import static earl.client.games.scs.bastogne.BastogneUnits.ge_KG902_10;
import static earl.client.games.scs.bastogne.BastogneUnits.ge_KG902_1_I;
import static earl.client.games.scs.bastogne.BastogneUnits.ge_KG902_2_I;
import static earl.client.games.scs.bastogne.BastogneUnits.ge_KG902_3_I;
import static earl.client.games.scs.bastogne.BastogneUnits.ge_KG902_5_130;
import static earl.client.games.scs.bastogne.BastogneUnits.ge_KG902_5_II;
import static earl.client.games.scs.bastogne.BastogneUnits.ge_KG902_6_II;
import static earl.client.games.scs.bastogne.BastogneUnits.ge_KG902_7_130;
import static earl.client.games.scs.bastogne.BastogneUnits.ge_KG902_7_II;
import static earl.client.games.scs.bastogne.BastogneUnits.ge_KG902_9;
import static earl.client.games.scs.bastogne.BastogneUnits.ge_KG902_Arty;
import static earl.client.games.scs.bastogne.BastogneUnits.us_101_327;
import static earl.client.games.scs.bastogne.BastogneUnits.us_501_A_1;
import static earl.client.games.scs.bastogne.BastogneUnits.us_501_B_1;
import static earl.client.games.scs.bastogne.BastogneUnits.us_501_C_1;
import static earl.client.games.scs.bastogne.BastogneUnits.us_501_D_2;
import static earl.client.games.scs.bastogne.BastogneUnits.us_501_E_2;
import static earl.client.games.scs.bastogne.BastogneUnits.us_501_F_2;
import static earl.client.games.scs.bastogne.BastogneUnits.us_501_G_3;
import static earl.client.games.scs.bastogne.BastogneUnits.us_501_H_3;
import static earl.client.games.scs.bastogne.BastogneUnits.us_58_Art;
import static earl.client.games.scs.bastogne.BastogneUnits.us_73_Art;
import static earl.client.games.scs.bastogne.BastogneUnits.us_CCR_C_482;
import static earl.client.games.scs.bastogne.BastogneUnits.us_CCR_HHC;
import static earl.client.games.scs.bastogne.BastogneUnits.us_Cherry_A_3;
import static earl.client.games.scs.bastogne.BastogneUnits.us_Cherry_C_20;
import static earl.client.games.scs.bastogne.BastogneUnits.us_Cherry_C_609;
import static earl.client.games.scs.bastogne.BastogneUnits.us_Cherry_D_3;
import static earl.client.games.scs.bastogne.BastogneUnits.us_Cherry_D_90;
import static earl.client.games.scs.bastogne.BastogneUnits.us_Cherry_HHC_3;
import static earl.client.games.scs.bastogne.BastogneUnits.us_Comb_C;
import static earl.client.games.scs.bastogne.BastogneUnits.us_Comb_D;
import static earl.client.games.scs.bastogne.BastogneUnits.us_SNAFU_AdHoc;

public class Bastogne implements Game, IsSerializable {
	private static final long serialVersionUID = 1L;
	private Board board = null;
	private String playerUS;
	private String playerGE;

	public Bastogne() {
		SCSHex[][] hexes = new SCSHex[56][36];
		MapTraits.setupTraits(hexes);
		for (int x = 0; x < hexes.length; x++) {
			for (int y = 0; y < hexes[x].length; y++) {
				if (hexes[x][y] == null) {
					hexes[x][y] = new SCSHex();
				}
			}
		}
		board = new SCSBoard(hexes);
	}

	public void setupScenarion52() {
//		if (System.currentTimeMillis() != 0) return;
		setup("h3717", us_Cherry_D_90, "Team Cherry (D/90 Arm Recon Platoon)");
		setup("h3717", us_Cherry_HHC_3, "Team Cherry (HHC/3 Mortar Platoon)");
		setup("h5021", us_SNAFU_AdHoc, "Ad Hoc Inf Co (SNAFU)");
		setup("h5022", us_CCR_C_482, "CCR (C/482 Arm AA Co)");
		setup("h4722", us_CCR_HHC, "CCR (HHC Arm Arty Platoon)");
		setup("h4922", us_58_Art, "58 Arty Bn");
		setup("h4924", us_Cherry_C_609, "Team Cherry (C/609 TD Platoon)");
		setup("h4923", us_Cherry_A_3, "Team Cherry (A/3 Arm Co)");
		setup("h4823", us_73_Art, "73 Arm Arty Bn");
		setup("h4622", us_Cherry_C_20, "Team Cherry (C/20 Arm Inf Co)");
		setup("h4522", us_Cherry_D_3, "Team Cherry (D/3 Arm Platoon)");
		setup("h3720", us_Comb_C, "Comb Eng Bn (C Co)");
		setup("h4019", us_Comb_D, "Comb Eng Bn (D Co)");

		String hexId = "US2";
		setup(hexId, us_501_A_1, "1st Bn 501 Abn Inf (A/1)");
		setup(hexId, us_501_B_1, "1st Bn 501 Abn Inf (B/1)");
		setup(hexId, us_501_C_1, "1st Bn 501 Abn Inf (C/1)");
		setup(hexId, us_501_D_2, "2nd Bn 501 Abn Inf (D/2)");
		setup(hexId, us_501_E_2, "2nd Bn 501 Abn Inf (E/2)");
		setup(hexId, us_501_F_2, "2nd Bn 501 Abn Inf (F/2)");
		setup(hexId, us_501_G_3, "3rd Bn 501 Abn Inf (G/3)");
		setup(hexId, us_501_H_3, "3rd Bn 501 Abn Inf (H/3)");
		setup(hexId, us_101_327, "377 Abn Arty Bn");

		setup(1, "D", ge_26VG_1_I_78, "26 VG I/78 1 Inf Co");
		setup(1, "D", ge_26VG_2_I_78, "26 VG I/78 2 Inf Co");
		setup(1, "D", ge_26VG_3_I_78, "26 VG I/78 3 Inf Co");
		setup(1, "D", ge_26VG_4_I_78, "26 VG I/78 4 Mortar Co");
		setup(1, "D", ge_26VG_S_I_78, "26 VG I/78 Sturm SMG Co");
		setup(1, "D", ge_26VG_5_II_78, "26 VG I/78 5 Inf Co");
		setup(1, "D", ge_26VG_6_II_78, "26 VG I/78 6 Inf Co");
		setup(1, "D", ge_26VG_7_II_78, "26 VG I/78 7 Inf Co");
		setup(1, "D", ge_26VG_8_II_78, "26 VG I/78 8 Mortar Co");
		setup(1, "D", ge_26VG_S_II_78, "26 VG I/78 Sturm SMG Co");

		setup(1, "E", ge_KG902_1_I, "KG 902 I Bn 1 PG Co");
		setup(1, "E", ge_KG902_2_I, "KG 902 I Bn 2 PG Co");
		setup(1, "E", ge_KG902_3_I, "KG 902 I Bn 3 PG Co");
		setup(1, "E", ge_KG902_5_II, "KG 902 II Bn 5 PG Co");
		setup(1, "E", ge_KG902_6_II, "KG 902 II Bn 6 PG Co");
		setup(1, "E", ge_KG902_7_II, "KG 902 II Bn 7 PG Co");
		setup(1, "E", ge_KG902_9, "KG 902 9 Inf Gun Co");
		setup(1, "E", ge_KG902_10, "KG 902 10 Pz Pio Co");
		setup(1, "E", ge_KG902_5_130, "KG 902 5/130 Pz Co");
		setup(1, "E", ge_KG902_7_130, "KG 902 7/130 Pz Co");
		setup(1, "E", ge_KG902_Arty, "KG 902 Arty Bn");

		setup(1, "E", ge_26VG_1_I_77, "26 VG I/77 1 Inf Co");
		setup(1, "E", ge_26VG_2_I_77, "26 VG I/77 2 Inf Co");
		setup(1, "E", ge_26VG_3_I_77, "26 VG I/77 3 Inf Co");
		setup(1, "E", ge_26VG_4_I_77, "26 VG I/77 4 Mortar Co");
		setup(1, "E", ge_26VG_S_I_77, "26 VG I/77 Sturm SMG Co");
		setup(1, "E", ge_26VG_5_II_77, "26 VG I/77 5 Inf Co");
		setup(1, "E", ge_26VG_6_II_77, "26 VG I/77 6 Inf Co");
		setup(1, "E", ge_26VG_7_II_77, "26 VG I/77 7 Inf Co");
		setup(1, "E", ge_26VG_8_II_77, "26 VG I/77 8 Mortar Co");
		setup(1, "E", ge_26VG_S_II_77, "26 VG I/77 Sturm SMG Co");

		setup(3, "E", ge_26VG_IV_26, "26 VG IV/26 Arty Bn");

		SCSMarker counter;
		for (int i = 0; i < 18; i++) {
			counter = new SCSMarker("us_dg" + i, "admin/misc_us-dg.png", BastogneSide.US);
			counter.setDescription("US Disorganized Units");
			board.place(new AreaRef("us_dg"), counter);
			counter = new SCSMarker("ge_dg" + i, "admin/misc_ge-dg.png", BastogneSide.GE);
			counter.setDescription("GE Disorganized Units");
			board.place(new AreaRef("ge_dg"), counter);
		}
	}

	private void setup(String hexId, BastogneUnits unit, String desc) {
		String id = unit.getId();
		String side = unit.name().substring(0, 2).toUpperCase();
		SCSCounter counter = new SCSCounter(id, unit.front, unit.back, BastogneSide.valueOf(side), unit.attack,
				unit.range, unit.defence, unit.movement);
		counter.setDescription(desc);
		board.place(new AreaRef(hexId), counter);
	}

	private void setup(int turn, String area, BastogneUnits unit, String desc) {
		String hexId = turn + area;
		setup(hexId, unit, desc);
	}

	public void setPlayer(BastogneSide side, String player) {
		if (side == BastogneSide.US) {
			this.playerUS = player;
		} else {
			this.playerGE = player;
		}
	}

	public String getPlayer(BastogneSide side) {
		if (side == BastogneSide.US) {
			return playerUS;
		} else {
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

	@Override
	public String[] getPlayers() {
		return new String[] { playerUS, playerGE };
	}

	public int getMovementCost(Hex target) {
		String info = null;//TODO: getHexInfo(target);
		int cost = 1;
		if(info != null) {
			if(info.contains("F")) {
				cost = 2;
			}
			if(info.contains("R") && !info.contains("C")) {
				cost +=1;
			}
		}
		return cost;
	}

	public int[] calculateOdds(Hex target, Collection<Hex> attacking) {
		List<Counter> defending = target.getStack();
		String hexInfo = null;//TODO:getHexInfo(target);
		float defence = getDefenceRawSum(defending);
		if (hexInfo != null) {
			boolean forrest = hexInfo.contains("F");
			boolean city = hexInfo.contains("C");
			if (forrest) {
				defence *= 2;
			}
			if (city) {
				defence *= 2;
			}
		}
		float attack = getAttackRawSum(attacking);
		float smaller = Math.min(attack, defence);
		int a = Math.round(attack / smaller);
		int b = Math.round(defence / smaller);
		int[] odds = { a, b };
		return odds;
	}

	private float getDefenceRawSum(Collection<Counter> defending) {
		float defence = 0;
		for (Counter counter : defending) {
			defence += ((SCSCounter) counter).getDefence();
		}
		return defence;
	}

	private float getAttackRawSum(Collection<Hex> list) {
		float attack = 0;
		for (Hex hex : list) {
			for (Counter c : hex.getStack()) {
				attack += ((SCSCounter) c).getAttack();
			}
		}
		return attack;
	}

	private static final String[][] CRT = {//@formatter:off
		{"     ","1:3 ","1:2 ","1:1 ","2:1 ","3:1 ","4:1 ","5:1"},
		{"2    ","A1r2","A1r2","A1r2","A1r1","A1r1","A1  ","A1  "},
		{"3    ","A1r2","A1r2","A1r1","A1r1","A1  ","A1  ","A1D1"},
		{"4    ","A1r2","A1r1","A1r1","A1  ","A1  ","A1D1","D1r1"},
		{"5    ","A1r1","A1r1","A1  ","A1  ","A1D1","D1r1","D1r2"},
		{"6    ","A1r1","A1  ","A1  ","A1D1","D1r1","D1r2","D1r2"},
		{"7    ","A1  ","A1  ","A1D1","D1r1","D1r2","D1r2","D1r3"},
		{"8    ","A1  ","A1  ","A1D1","D1r1","D1r2","D1r2","D1r3"},//same
		{"9    ","A1  ","A1D1","D1r1","D1r2","D1r2","D1r3","D1r4"},
		{"10   ","A1  ","D1r1","D1r2","D1r2","D1r3","D1r3","D2r5"}, 
		{"11   ","A1  ","D1r1","D1r2","D1r2","D1r3","D1r3","D2r5"},//same 
		{"12   ","A1D1","D1r2","D1r2","D1r3","D1r3","D2r4","D2r6"},		
	};//@formatter:on

	public CombatResult getCombatResult(int[] odds, int sum) {
		int col = getCRTColumn(odds);
		int row = sum - 1;
		String value = CRT[row][col];
		return new CombatResult(value);
	}

	private int getCRTColumn(int[] odds) {
		if (3 < odds[1]) {
			return 1;
		} else if (odds[0] > 5) {
			return 7;
		}
		String oddsValue = odds[0] + ":" + odds[1];
		for (int col = 1; col < CRT[0].length; col++) {
			if (oddsValue.equals(CRT[0][col].trim())) {
				return col;
			}
		}
		throw new EarlException("invalid odds:" + oddsValue);
	}

	public void load(Collection<Operation> ops, SVGDisplay display) {
		setupScenarion52();
		for (Operation op : ops) {
			op.updateBoard(board);
			if(display != null) {
				op.draw(board, display);
			}
		}		
	}

}
