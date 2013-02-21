package webboards.client.games.scs.bastogne;

import java.util.Collection;

import webboards.client.data.Board;
import webboards.client.data.CounterInfo;
import webboards.client.games.Area;
import webboards.client.games.Hex;
import webboards.client.games.Position;
import webboards.client.games.scs.SCSCounter;
import static webboards.client.games.scs.bastogne.BastogneUnits.ge_26VG_1_I_77;
import static webboards.client.games.scs.bastogne.BastogneUnits.ge_26VG_1_I_78;
import static webboards.client.games.scs.bastogne.BastogneUnits.ge_26VG_2_I_77;
import static webboards.client.games.scs.bastogne.BastogneUnits.ge_26VG_2_I_78;
import static webboards.client.games.scs.bastogne.BastogneUnits.ge_26VG_3_I_77;
import static webboards.client.games.scs.bastogne.BastogneUnits.ge_26VG_3_I_78;
import static webboards.client.games.scs.bastogne.BastogneUnits.ge_26VG_4_I_77;
import static webboards.client.games.scs.bastogne.BastogneUnits.ge_26VG_4_I_78;
import static webboards.client.games.scs.bastogne.BastogneUnits.ge_26VG_5_II_77;
import static webboards.client.games.scs.bastogne.BastogneUnits.ge_26VG_5_II_78;
import static webboards.client.games.scs.bastogne.BastogneUnits.ge_26VG_6_II_77;
import static webboards.client.games.scs.bastogne.BastogneUnits.ge_26VG_6_II_78;
import static webboards.client.games.scs.bastogne.BastogneUnits.ge_26VG_7_II_77;
import static webboards.client.games.scs.bastogne.BastogneUnits.ge_26VG_7_II_78;
import static webboards.client.games.scs.bastogne.BastogneUnits.ge_26VG_8_II_77;
import static webboards.client.games.scs.bastogne.BastogneUnits.ge_26VG_8_II_78;
import static webboards.client.games.scs.bastogne.BastogneUnits.ge_26VG_IV_26;
import static webboards.client.games.scs.bastogne.BastogneUnits.ge_26VG_S_II_77;
import static webboards.client.games.scs.bastogne.BastogneUnits.ge_26VG_S_II_78;
import static webboards.client.games.scs.bastogne.BastogneUnits.ge_26VG_S_I_77;
import static webboards.client.games.scs.bastogne.BastogneUnits.ge_26VG_S_I_78;
import static webboards.client.games.scs.bastogne.BastogneUnits.ge_KG902_10;
import static webboards.client.games.scs.bastogne.BastogneUnits.ge_KG902_1_I;
import static webboards.client.games.scs.bastogne.BastogneUnits.ge_KG902_2_I;
import static webboards.client.games.scs.bastogne.BastogneUnits.ge_KG902_3_I;
import static webboards.client.games.scs.bastogne.BastogneUnits.ge_KG902_5_130;
import static webboards.client.games.scs.bastogne.BastogneUnits.ge_KG902_5_II;
import static webboards.client.games.scs.bastogne.BastogneUnits.ge_KG902_6_II;
import static webboards.client.games.scs.bastogne.BastogneUnits.ge_KG902_7_130;
import static webboards.client.games.scs.bastogne.BastogneUnits.ge_KG902_7_II;
import static webboards.client.games.scs.bastogne.BastogneUnits.ge_KG902_9;
import static webboards.client.games.scs.bastogne.BastogneUnits.ge_KG902_Arty;
import static webboards.client.games.scs.bastogne.BastogneUnits.us_101_327;
import static webboards.client.games.scs.bastogne.BastogneUnits.us_501_A_1;
import static webboards.client.games.scs.bastogne.BastogneUnits.us_501_B_1;
import static webboards.client.games.scs.bastogne.BastogneUnits.us_501_C_1;
import static webboards.client.games.scs.bastogne.BastogneUnits.us_501_D_2;
import static webboards.client.games.scs.bastogne.BastogneUnits.us_501_E_2;
import static webboards.client.games.scs.bastogne.BastogneUnits.us_501_F_2;
import static webboards.client.games.scs.bastogne.BastogneUnits.us_501_G_3;
import static webboards.client.games.scs.bastogne.BastogneUnits.us_501_H_3;
import static webboards.client.games.scs.bastogne.BastogneUnits.us_58_Art;
import static webboards.client.games.scs.bastogne.BastogneUnits.us_73_Art;
import static webboards.client.games.scs.bastogne.BastogneUnits.us_CCR_C_482;
import static webboards.client.games.scs.bastogne.BastogneUnits.us_CCR_HHC;
import static webboards.client.games.scs.bastogne.BastogneUnits.us_Cherry_A_3;
import static webboards.client.games.scs.bastogne.BastogneUnits.us_Cherry_C_20;
import static webboards.client.games.scs.bastogne.BastogneUnits.us_Cherry_C_609;
import static webboards.client.games.scs.bastogne.BastogneUnits.us_Cherry_D_3;
import static webboards.client.games.scs.bastogne.BastogneUnits.us_Cherry_D_90;
import static webboards.client.games.scs.bastogne.BastogneUnits.us_Cherry_HHC_3;
import static webboards.client.games.scs.bastogne.BastogneUnits.us_Comb_C;
import static webboards.client.games.scs.bastogne.BastogneUnits.us_Comb_D;
import static webboards.client.games.scs.bastogne.BastogneUnits.us_SNAFU_AdHoc;

public class BastogneCampain {
	private final Board board;

	public BastogneCampain(Board board) {
		this.board = board;
	}

	public void battleForNoville() {
		//US:
		setup("34.27", BastogneUnits.us_Desobry_C_609);
		setup("34.26", BastogneUnits.us_Comb_A);
		setup("38.31", BastogneUnits.us_Desobry_B_20);
		setup("36.32", BastogneUnits.us_Desobry_D_3);
		setup("36.30", BastogneUnits.us_Desobry_B_3);
		setup("35.30", BastogneUnits.us_Desobry_D_90);

		setup("28.30", BastogneUnits.us_420_Art);
		setup("28.29", BastogneUnits.us_755_Art);
		setup("28.28", BastogneUnits.us_969_Art);

		setup(2, "33.24", BastogneUnits.us_506_A_1);
		setup(2, "33.24", BastogneUnits.us_506_B_1);
		setup(2, "35.24", BastogneUnits.us_506_C_1);

		//German
		setup(2, "43.29", BastogneUnits.ge_26VG_II_26);
		setup(2, "43.29", BastogneUnits.ge_26VG_III_26);
		setup(2, "43.29", BastogneUnits.ge_766_Art);

		setup(2, "41.28", BastogneUnits.ge_vBohm_2Aufk);
		setup(2, "41.31", BastogneUnits.ge_vBohm_3Aufk);
		setup(2, "41.31", BastogneUnits.ge_vBohm_HQ);
		setup(2, "41.31", BastogneUnits.ge_vBohm_Pz);
		setup(2, "41.35", BastogneUnits.ge_vBohm_Wpn);

		setup(3, "42.28", BastogneUnits.ge_Coch_2 );
		setup(3, "42.28", BastogneUnits.ge_Coch_3 );
		setup(3, "42.28", BastogneUnits.ge_Coch_4 );
		setup(3, "42.28", BastogneUnits.ge_Coch_3_38 );
		setup(3, "42.28", BastogneUnits.ge_Coch_1_273 );
	}

	public void battleForLongvilly() {
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

		setup(2, "US", us_501_A_1, "1st Bn 501 Abn Inf (A/1)");
		setup(2, "US", us_501_B_1, "1st Bn 501 Abn Inf (B/1)");
		setup(2, "US", us_501_C_1, "1st Bn 501 Abn Inf (C/1)");
		setup(2, "US", us_501_D_2, "2nd Bn 501 Abn Inf (D/2)");
		setup(2, "US", us_501_E_2, "2nd Bn 501 Abn Inf (E/2)");
		setup(2, "US", us_501_F_2, "2nd Bn 501 Abn Inf (F/2)");
		setup(2, "US", us_501_G_3, "3rd Bn 501 Abn Inf (G/3)");
		setup(2, "US", us_501_H_3, "3rd Bn 501 Abn Inf (H/3)");
		setup(2, "US", us_101_327, "377 Abn Arty Bn");

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
	}

	private void setup(String hexId, BastogneUnits unit, String desc) {
		Position pos = Hex.fromSVGId(hexId);
		setup(unit, desc, pos);
	}

	private void setup(BastogneUnits unit, String desc, Position pos) {
		String id = unit.getId();
		String side = unit.name().substring(0, 2).toUpperCase();
		SCSCounter counter = new SCSCounter(id, unit.front, unit.back, BastogneSide.valueOf(side), unit.attack,
				unit.range, unit.artyType, unit.defence, unit.movement);
		counter.setDescription(desc);
		board.place(pos, counter);
	}

	private void setup(int turn, String position, BastogneUnits unit) {
		setup(unit, null, Hex.fromSVGId(position));
	}
	
	private void setup(String position, BastogneUnits unit) {
		setup(0, position, unit);
	}


	private void setup(int turn, String areaId, BastogneUnits unit, String desc) {
		Area area = new Area(turn + areaId);
		setup(unit, desc, area);
	}

	public void dumpUsed() {
		Collection<CounterInfo> counters = board.getCounters();
		for (CounterInfo ci : counters) {
			SCSCounter c = (SCSCounter) ci;
			if (c.getOwner() == BastogneSide.GE) {
				System.out.print(" *" + c.ref() + "_f.png");
			}
		}
	}
}
