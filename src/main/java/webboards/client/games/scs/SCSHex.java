package webboards.client.games.scs;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import webboards.client.data.CounterInfo;
import webboards.client.data.HexInfo;
import webboards.client.games.Hex;
import webboards.client.games.Position;
import webboards.client.games.scs.bastogne.HexTraits;

public class SCSHex extends HexInfo {
	private static final long serialVersionUID = 1L;
	private Set<HexTraits> traits = null;

	public SCSHex() {
		traits = Collections.emptySet();
	}

	public SCSHex(HexTraits... traits) {
		this.traits = new HashSet<HexTraits>(Arrays.asList(traits));
	}

	public Set<HexTraits> getTraits() {
		return traits;
	}

	public static boolean isAdjacent(Position pa, Position pb) {
		if (pa instanceof Hex && pb instanceof Hex) {
			Hex a = (Hex) pa;
			Hex b = (Hex) pb;
			int o = (b.x % 2 == 0) ? 0 : -1;
			//@formatter:off
			return 					(equals(a, b.x,b.y+1) || 
					equals(a, b.x-1,b.y+1+o) || equals(a, b.x+1,b.y+1+o) ||	
					equals(a, b.x-1,b.y+o)   ||	equals(a, b.x+1,b.y+o) ||
									equals(a, b.x,b.y-1));
			//@formatter:on
		} else {
			return false;
		}
	}
	
	private static boolean equals(Hex a, int x, int y) {
		return a.x == x && a.y == y;
	}

	public float getDefenceCombatModifier() {
		int modifier = 1;
		if(traits.contains(HexTraits.FOREST)) {
			modifier *= 2;
		}
		if(traits.contains(HexTraits.CITY)) {
			modifier *= 2;
		}
		if(hasDG()) {
			modifier /= 2;
		}
		return modifier;
	}

	private boolean hasDG() {
		for (CounterInfo piece : pieces) {
			if(piece instanceof SCSMarker) {
				//TODO: check if marker is realy DG
				return true;
			}
		}
		return false;
	}

	public int getBarrageModifier() {
		if(traits.contains(HexTraits.FOREST)) {
			return 1;
		}
		if(traits.contains(HexTraits.CITY)) {
			return -1;
		}
		return 0;
	}
}
