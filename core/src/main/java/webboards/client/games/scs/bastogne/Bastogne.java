package webboards.client.games.scs.bastogne;

import java.util.HashMap;
import java.util.Map;

import webboards.client.data.Game;
import webboards.client.data.Side;
import webboards.client.ex.WebBoardsException;
import webboards.client.games.Area;
import webboards.client.games.Scenario;
import webboards.client.games.scs.CombatResult;
import webboards.client.games.scs.SCSBoard;
import webboards.client.games.scs.SCSHex;
import webboards.client.games.scs.SCSMarker;

public class Bastogne implements Game  {
	private static final int DG_POOL = 18;
	private static final long serialVersionUID = 1L;

	public Bastogne() {
	}
	
	@Override
	public SCSBoard start(Scenario scenario) {
		SCSHex[][] hexes = new SCSHex[56][36];
		MapTraits.setupTraits(hexes);
		for (int x = 0; x < hexes.length; x++) {
			for (int y = 0; y < hexes[x].length; y++) {
				if (hexes[x][y] == null) {
					hexes[x][y] = new SCSHex();
				}
			}
		}
		SCSBoard board = new SCSBoard(hexes);
		scenario.setup(board);
		
		Map<BastogneSide, Area> dg = new HashMap<BastogneSide, Area>();
		dg.put(BastogneSide.US, new Area("us_dg"));
		dg.put(BastogneSide.GE, new Area("ge_dg"));
		SCSMarker counter;
		for (int i = 0; i < DG_POOL; i++) {
			for (BastogneSide side: BastogneSide.values()) {
				String low = side.name().toLowerCase();
				counter = new SCSMarker(low+"_dg" + i, "admin/misc_"+low+"-dg.png", side);
				counter.setDescription(side+" Disorganized Units");
				Area area = dg.get(side);
				board.setup(area, counter);				
			}
		}
		return board;
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

	public static CombatResult getCombatResult(int[] odds, int sum) {
		int col = getCRTColumn(odds);
		int row = sum - 1;
		String value = CRT[row][col];
		return new CombatResult(value);
	}

	private static int getCRTColumn(int[] odds) {
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
		throw new WebBoardsException("invalid odds:" + oddsValue);
	}

	@Override
	public Side[] getSides() {
		return BastogneSide.values();
	}
}
