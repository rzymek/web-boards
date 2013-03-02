package webboards.client.games;

import java.io.Serializable;
import java.util.Collection;

import webboards.client.data.Board;
import webboards.client.data.CounterInfo;
import webboards.client.games.scs.SCSCounter;
import webboards.client.games.scs.bastogne.BastogneSide;
import webboards.client.games.scs.bastogne.BastogneUnits;

public abstract class Scenario implements Serializable {
	private static final long serialVersionUID = 1L;
	protected transient Board board;

	protected void setup(BastogneUnits unit, Position pos) {
		String id = unit.getId();
		String side = unit.name().substring(0, 2).toUpperCase();
		SCSCounter counter = new SCSCounter(id, unit.front, unit.back, BastogneSide.valueOf(side), unit.attack,
				unit.range, unit.artyType, unit.defence, unit.movement);
		board.place(pos, counter);
	}

	protected void setupArea(int turn, String areaId, BastogneUnits unit) {
		Area area = new Area(turn + areaId);
		setup(unit, area);
	}

	protected void setupHex(int turn, String hex, BastogneUnits unit) {
		//TODO: include turn
		String[] split = hex.split("[.]");
		int x = Integer.parseInt(split[0]);
		int y = Integer.parseInt(split[1]);
		setup(unit, new Hex(x,y));
	}

	protected void setupHex(String hex, BastogneUnits unit) {
		setupHex(0, hex, unit);
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

	public abstract void setup(Board board);
}
