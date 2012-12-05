package earl.client.games;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import earl.client.ClientEngine;
import earl.client.data.Board;
import earl.client.data.Counter;
import earl.client.data.Hex;
import static earl.client.games.BastogneUnits.ge_26VG_1_I_77;
import static earl.client.games.BastogneUnits.ge_26VG_1_I_78;
import static earl.client.games.BastogneUnits.ge_26VG_2_I_77;
import static earl.client.games.BastogneUnits.ge_26VG_2_I_78;
import static earl.client.games.BastogneUnits.ge_26VG_3_I_77;
import static earl.client.games.BastogneUnits.ge_26VG_3_I_78;
import static earl.client.games.BastogneUnits.ge_26VG_4_I_77;
import static earl.client.games.BastogneUnits.ge_26VG_4_I_78;
import static earl.client.games.BastogneUnits.ge_26VG_5_II_77;
import static earl.client.games.BastogneUnits.ge_26VG_5_II_78;
import static earl.client.games.BastogneUnits.ge_26VG_6_II_77;
import static earl.client.games.BastogneUnits.ge_26VG_6_II_78;
import static earl.client.games.BastogneUnits.ge_26VG_7_II_77;
import static earl.client.games.BastogneUnits.ge_26VG_7_II_78;
import static earl.client.games.BastogneUnits.ge_26VG_8_II_77;
import static earl.client.games.BastogneUnits.ge_26VG_8_II_78;
import static earl.client.games.BastogneUnits.ge_26VG_IV_26;
import static earl.client.games.BastogneUnits.ge_26VG_S_II_77;
import static earl.client.games.BastogneUnits.ge_26VG_S_II_78;
import static earl.client.games.BastogneUnits.ge_26VG_S_I_77;
import static earl.client.games.BastogneUnits.ge_26VG_S_I_78;
import static earl.client.games.BastogneUnits.ge_KG902_10;
import static earl.client.games.BastogneUnits.ge_KG902_1_I;
import static earl.client.games.BastogneUnits.ge_KG902_2_I;
import static earl.client.games.BastogneUnits.ge_KG902_3_I;
import static earl.client.games.BastogneUnits.ge_KG902_5_130;
import static earl.client.games.BastogneUnits.ge_KG902_5_II;
import static earl.client.games.BastogneUnits.ge_KG902_6_II;
import static earl.client.games.BastogneUnits.ge_KG902_7_130;
import static earl.client.games.BastogneUnits.ge_KG902_7_II;
import static earl.client.games.BastogneUnits.ge_KG902_9;
import static earl.client.games.BastogneUnits.ge_KG902_Arty;
import static earl.client.games.BastogneUnits.us_101_327;
import static earl.client.games.BastogneUnits.us_501_A_1;
import static earl.client.games.BastogneUnits.us_501_B_1;
import static earl.client.games.BastogneUnits.us_501_C_1;
import static earl.client.games.BastogneUnits.us_501_D_2;
import static earl.client.games.BastogneUnits.us_501_E_2;
import static earl.client.games.BastogneUnits.us_501_F_2;
import static earl.client.games.BastogneUnits.us_501_G_3;
import static earl.client.games.BastogneUnits.us_501_H_3;
import static earl.client.games.BastogneUnits.us_58_Art;
import static earl.client.games.BastogneUnits.us_73_Art;
import static earl.client.games.BastogneUnits.us_CCR_C_482;
import static earl.client.games.BastogneUnits.us_CCR_HHC;
import static earl.client.games.BastogneUnits.us_Cherry_A_3;
import static earl.client.games.BastogneUnits.us_Cherry_C_20;
import static earl.client.games.BastogneUnits.us_Cherry_C_609;
import static earl.client.games.BastogneUnits.us_Cherry_D_3;
import static earl.client.games.BastogneUnits.us_Cherry_D_90;
import static earl.client.games.BastogneUnits.us_Cherry_HHC_3;
import static earl.client.games.BastogneUnits.us_Comb_C;
import static earl.client.games.BastogneUnits.us_Comb_D;
import static earl.client.games.BastogneUnits.us_SNAFU_AdHoc;

public class Bastogne implements Game, Serializable {
	private Board board = null;
	private String playerUS;
	private String playerGE;
	private Map<String, String> mapInfo;
	public Map<String, String> attacks = Collections.emptyMap();

	public Bastogne() {
		board = new Board();
	}

	public void setupScenarion52() {
//		if (System.currentTimeMillis() != 0) return;
		setup("37.17", us_Cherry_D_90, "Team Cherry (D/90 Arm Recon Platoon)");
		setup("37.17", us_Cherry_HHC_3, "Team Cherry (HHC/3 Mortar Platoon)");
		setup("50.21", us_SNAFU_AdHoc, "Ad Hoc Inf Co (SNAFU)");
		setup("50.22", us_CCR_C_482, "CCR (C/482 Arm AA Co)");
		setup("47.22", us_CCR_HHC, "CCR (HHC Arm Arty Platoon)");
		setup("49.22", us_58_Art, "58 Arty Bn");
		setup("49.24", us_Cherry_C_609, "Team Cherry (C/609 TD Platoon)");
		setup("49.23", us_Cherry_A_3, "Team Cherry (A/3 Arm Co)");
		setup("48.23", us_73_Art, "73 Arm Arty Bn");
		setup("46.22", us_Cherry_C_20, "Team Cherry (C/20 Arm Inf Co)");
		setup("45.22", us_Cherry_D_3, "Team Cherry (D/3 Arm Platoon)");
		setup("37.20", us_Comb_C, "Comb Eng Bn (C Co)");
		setup("40.19", us_Comb_D, "Comb Eng Bn (D Co)");

		String hexId = "33.16-21";
		setup(hexId, us_501_A_1, "1st Bn 501 Abn Inf (A/1)");
		setup(hexId, us_501_B_1, "1st Bn 501 Abn Inf (B/1)");
		setup(hexId, us_501_C_1, "1st Bn 501 Abn Inf (C/1)");
		setup(hexId, us_501_D_2, "2nd Bn 501 Abn Inf (D/2)");
		setup(hexId, us_501_E_2, "2nd Bn 501 Abn Inf (E/2)");
		setup(hexId, us_501_F_2, "2nd Bn 501 Abn Inf (F/2)");
		setup(hexId, us_501_G_3, "3rd Bn 501 Abn Inf (G/3)");
		setup(hexId, us_501_H_3, "3rd Bn 501 Abn Inf (H/3)");
		setup(hexId, us_101_327, "377 Abn Arty Bn");
		// setup(hexId, us_Comb_C, "Comb Eng Bn (C Co)");
		// setup(hexId, us_Comb_D, "Comb Eng Bn (D Co)");

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
			board.place("us_dg", counter);
			counter = new SCSMarker("ge_dg" + i, "admin/misc_ge-dg.png", BastogneSide.GE);
			counter.setDescription("GE Disorganized Units");
			board.place("ge_dg", counter);
		}
	}

	private void setup(String hexId, BastogneUnits unit, String desc) {
		String id = unit.getId();
		String side = unit.name().substring(0,2).toUpperCase();
		SCSCounter counter = new SCSCounter(id, unit.front, unit.back, BastogneSide.valueOf(side),
				unit.attack, unit.range, unit.defence, unit.movement);
		counter.setDescription(desc);
		board.place(hexId, counter);
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

	public void setMapInfo(Map<String, String> mapInfo) {
		this.mapInfo = mapInfo;		
	}
	
	public Map<String, String> getMapInfo() {
		return mapInfo;
	}
	
	public int[] calculateOdds(Hex target) {
		Set<Counter> attackers = getAttackers(target);
		List<Counter> defending = target.getStack();
		ClientEngine.log(attackers + " attacks " + defending);
		String hexId = target.getId();
		String hexInfo = mapInfo.get(hexId);
		float defence = getDefence(defending);
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
		float attack = getAttack(attackers);
		float smaller = Math.min(attack, defence);
		int a = Math.round(attack / smaller);
		int b = Math.round(defence / smaller);
		int[] odds = {a,b};
		return odds;
	}

	private float getAttack(Set<Counter> attackers) {
		float attack = 0;
		for (Counter a : attackers) {
			attack += ((SCSCounter)a).getAttack();
		}
		return attack;
	}


	private float getDefence(List<Counter> defending) {
		float defence = 0;
		for (Counter counter : defending) {
			defence += ((SCSCounter)counter).getDefence();
		}
		return defence;
	}

	private Set<Counter> getAttackers(Hex defending) {
		Set<Counter> attackers = new HashSet<Counter>();
		for (Entry<String, String> attack : attacks.entrySet()) {
			if(attack.getValue().equals(defending.getId())) {
				List<Counter> stack = board.getHex(attack.getKey()).getStack();
				attackers.addAll(stack);
			}
		}
		return attackers;
	}
}