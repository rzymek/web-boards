package webboards.client.games.scs.bastogne.scenarios;

import webboards.client.data.Board;
import webboards.client.games.Scenario;
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

public class BattleForLongvilly extends Scenario {
	private static final long serialVersionUID = 1L;

	@Override
	public void setup(Board board) {
		this.board = board;
		setupHex("37.17", us_Cherry_D_90);//Team Cherry (D/90 Arm Recon Platoon)
		setupHex("37.17", us_Cherry_HHC_3);//Team Cherry (HHC/3 Mortar Platoon)
		setupHex("50.21", us_SNAFU_AdHoc);//Ad Hoc Inf Co (SNAFU)
		setupHex("50.22", us_CCR_C_482);//CCR (C/482 Arm AA Co)
		setupHex("47.22", us_CCR_HHC);//CCR (HHC Arm Arty Platoon)
		setupHex("49.22", us_58_Art);//58 Arty Bn
		setupHex("49.24", us_Cherry_C_609);//Team Cherry (C/609 TD Platoon)
		setupHex("49.23", us_Cherry_A_3);//Team Cherry (A/3 Arm Co)
		setupHex("48.23", us_73_Art);//73 Arm Arty Bn
		setupHex("46.22", us_Cherry_C_20);//Team Cherry (C/20 Arm Inf Co)
		setupHex("45.22", us_Cherry_D_3);//Team Cherry (D/3 Arm Platoon)
		setupHex("37.20", us_Comb_C);//, "Comb Eng Bn (C Co)
		setupHex("40.19", us_Comb_D);//, "Comb Eng Bn (D Co)

		setupArea(2, "US", us_501_A_1);// 1st Bn 501 Abn Inf (A/1)
		setupArea(2, "US", us_501_B_1);// 1st Bn 501 Abn Inf (B/1)
		setupArea(2, "US", us_501_C_1);// 1st Bn 501 Abn Inf (C/1)
		setupArea(2, "US", us_501_D_2);// 2nd Bn 501 Abn Inf (D/2)
		setupArea(2, "US", us_501_E_2);// 2nd Bn 501 Abn Inf (E/2)
		setupArea(2, "US", us_501_F_2);// 2nd Bn 501 Abn Inf (F/2)
		setupArea(2, "US", us_501_G_3);// 3rd Bn 501 Abn Inf (G/3)
		setupArea(2, "US", us_501_H_3);// 3rd Bn 501 Abn Inf (H/3)
		setupArea(2, "US", us_101_327);// 377 Abn Arty Bn

		setupArea(1, "D", ge_26VG_1_I_78);// "26 VG I/78 1 Inf Co
		setupArea(1, "D", ge_26VG_2_I_78);// "26 VG I/78 2 Inf Co
		setupArea(1, "D", ge_26VG_3_I_78);// "26 VG I/78 3 Inf Co
		setupArea(1, "D", ge_26VG_4_I_78);// "26 VG I/78 4 Mortar Co
		setupArea(1, "D", ge_26VG_S_I_78);// "26 VG I/78 Sturm SMG Co
		setupArea(1, "D", ge_26VG_5_II_78);// "26 VG I/78 5 Inf Co
		setupArea(1, "D", ge_26VG_6_II_78);// "26 VG I/78 6 Inf Co
		setupArea(1, "D", ge_26VG_7_II_78);// "26 VG I/78 7 Inf Co
		setupArea(1, "D", ge_26VG_8_II_78);// "26 VG I/78 8 Mortar Co
		setupArea(1, "D", ge_26VG_S_II_78);// "26 VG I/78 Sturm SMG Co

		setupArea(1, "E", ge_KG902_1_I);// "KG 902 I Bn 1 PG Co
		setupArea(1, "E", ge_KG902_2_I);// "KG 902 I Bn 2 PG Co
		setupArea(1, "E", ge_KG902_3_I);// "KG 902 I Bn 3 PG Co
		setupArea(1, "E", ge_KG902_5_II);// "KG 902 II Bn 5 PG Co
		setupArea(1, "E", ge_KG902_6_II);// "KG 902 II Bn 6 PG Co
		setupArea(1, "E", ge_KG902_7_II);// "KG 902 II Bn 7 PG Co
		setupArea(1, "E", ge_KG902_9);// "KG 902 9 Inf Gun Co
		setupArea(1, "E", ge_KG902_10);// "KG 902 10 Pz Pio Co
		setupArea(1, "E", ge_KG902_5_130);// "KG 902 5/130 Pz Co
		setupArea(1, "E", ge_KG902_7_130);// "KG 902 7/130 Pz Co
		setupArea(1, "E", ge_KG902_Arty);// "KG 902 Arty Bn

		setupArea(1, "E", ge_26VG_1_I_77);// "26 VG I/77 1 Inf Co
		setupArea(1, "E", ge_26VG_2_I_77);// "26 VG I/77 2 Inf Co
		setupArea(1, "E", ge_26VG_3_I_77);// "26 VG I/77 3 Inf Co
		setupArea(1, "E", ge_26VG_4_I_77);// "26 VG I/77 4 Mortar Co
		setupArea(1, "E", ge_26VG_S_I_77);// "26 VG I/77 Sturm SMG Co
		setupArea(1, "E", ge_26VG_5_II_77);// "26 VG I/77 5 Inf Co
		setupArea(1, "E", ge_26VG_6_II_77);// "26 VG I/77 6 Inf Co
		setupArea(1, "E", ge_26VG_7_II_77);// "26 VG I/77 7 Inf Co
		setupArea(1, "E", ge_26VG_8_II_77);// "26 VG I/77 8 Mortar Co
		setupArea(1, "E", ge_26VG_S_II_77);// "26 VG I/77 Sturm SMG Co

		setupArea(3, "E", ge_26VG_IV_26);// "26 VG IV/26 Arty Bn
	}

}
