package webboards.client.games.scs.bastogne.scenarios;

import webboards.client.data.Board;
import webboards.client.games.Scenario;
import webboards.client.games.scs.bastogne.BastogneUnits;

public class BattleForNoville extends Scenario {
	private static final long serialVersionUID = 1L;

	@Override
	public void setup(Board board) {
		this.board = board;
		//US:
		setupHex("34.27", BastogneUnits.us_Desobry_C_609);
		setupHex("34.26", BastogneUnits.us_Comb_A);
		setupHex("38.31", BastogneUnits.us_Desobry_B_20);
		setupHex("36.32", BastogneUnits.us_Desobry_D_3);
		setupHex("36.30", BastogneUnits.us_Desobry_B_3);
		setupHex("35.30", BastogneUnits.us_Desobry_D_90);

		setupHex("28.30", BastogneUnits.us_420_Art);
		setupHex("28.29", BastogneUnits.us_755_Art);
		setupHex("28.28", BastogneUnits.us_969_Art);

		setupHex(2, "33.24", BastogneUnits.us_506_A_1);
		setupHex(2, "33.24", BastogneUnits.us_506_B_1);
		setupHex(2, "35.24", BastogneUnits.us_506_C_1);

		//German
		setupHex(2, "43.29", BastogneUnits.ge_26VG_II_26);
		setupHex(2, "43.29", BastogneUnits.ge_26VG_III_26);
		setupHex(2, "43.29", BastogneUnits.ge_766_Art);

		setupHex(2, "41.28", BastogneUnits.ge_vBohm_2Aufk);
		setupHex(2, "41.31", BastogneUnits.ge_vBohm_3Aufk);
		setupHex(2, "41.31", BastogneUnits.ge_vBohm_HQ);
		setupHex(2, "41.31", BastogneUnits.ge_vBohm_Pz);
		setupHex(2, "41.35", BastogneUnits.ge_vBohm_Wpn);

		setupHex(3, "42.28", BastogneUnits.ge_Coch_2);
		setupHex(3, "42.28", BastogneUnits.ge_Coch_3);
		setupHex(3, "42.28", BastogneUnits.ge_Coch_4);
		setupHex(3, "42.28", BastogneUnits.ge_Coch_3_38);
		setupHex(3, "42.28", BastogneUnits.ge_Coch_1_273);
	}
}
