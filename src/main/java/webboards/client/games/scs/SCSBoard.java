package webboards.client.games.scs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import webboards.client.data.Board;
import webboards.client.ex.EarlException;
import webboards.client.games.Hex;
import webboards.client.games.Position;
import webboards.client.utils.Browser;

public class SCSBoard extends Board implements Serializable {
	private static final long serialVersionUID = 2L;
	private SCSHex[][] hexes;
	protected SCSHex[] workaround;
	protected Map<String, SCSHex> areas = new HashMap<String, SCSHex>();
	protected Map<Hex, Hex> attacks = new HashMap<Hex, Hex>();
	protected Map<SCSCounter, Hex> barrages = new HashMap<SCSCounter, Hex>();
	public int phase = 0;
	public int turn = 0;

	protected SCSBoard() {
	}

	public SCSBoard(SCSHex[][] hexes) {
		this.hexes = hexes;
	}

	public SCSHex get(int x, int y) {
		return hexes[x][y];
	}

	@Override
	public SCSHex getInfo(Position ref) {
		if (ref instanceof Hex) {
			Hex xy = (Hex) ref;
			return hexes[xy.x][xy.y];
		} else {
			SCSHex hex = areas.get(ref.getSVGId());
			if (hex == null) {
				areas.put(ref.getSVGId(), hex = new SCSHex());
			}
			return hex;
		}
	}

	public void declareAttack(Hex attacking, Hex defending) {
		attacks.put(attacking, defending);
	}

	public void undeclareAttack(Hex attacking) {
		attacks.remove(attacking);
	}

	public Collection<Hex> getAttacking(Hex target) {
		Collection<Hex> result = new HashSet<Hex>();
		for (Entry<Hex, Hex> attack : attacks.entrySet()) {
			Hex attacking = attack.getKey();
			Hex defending = attack.getValue();
			if (defending.equals(target)) {
				result.add(attacking);
			}
		}
		return result;
	}

	public boolean isDeclaredAttackOn(Position pos) {
		return attacks.values().contains(pos);
	}

	public Hex getAttacks(Hex from) {
		return attacks.get(from);
	}

	public Collection<SCSHex> getAttackingInfo(Hex target) {
		return getInfo(getAttacking(target));
	}

	public Collection<SCSHex> getInfo(Collection<Hex> hexes) {
		Collection<SCSHex> info = new ArrayList<SCSHex>(hexes.size());
		for (Hex hex : hexes) {
			info.add(getInfo(hex));
		}
		return info;
	}

	public void clearAttacksOn(Hex target) {
		Browser.console(attacks.toString());
		Iterator<Entry<Hex, Hex>> iterator = attacks.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<Hex, Hex> e = iterator.next();
			Hex defending = e.getValue();
			if (defending.equals(target)) {
				iterator.remove();
			}
		}
		Browser.console(attacks.toString());
	}

	public void declareBarrage(SCSCounter who, Hex target) {
		barrages.put(who, target);
	}

	public Collection<SCSCounter> getBarragesOn(Hex target) {
		Collection<SCSCounter> result = new HashSet<SCSCounter>();
		for (Entry<SCSCounter, Hex> attack : barrages.entrySet()) {
			SCSCounter attacking = attack.getKey();
			Hex defending = attack.getValue();
			if (defending.equals(target)) {
				result.add(attacking);
			}
		}
		return result;
	}

	public void clearBarrageOf(SCSCounter attacing) {
		barrages.remove(attacing);
	}

	private static float getDefenceRawSum(Collection<SCSCounter> defending) {
		float defence = 0;
		for (SCSCounter counter : defending) {
			defence += counter.getDefence();
		}
		return defence;
	}

	private static float getAttackRawSum(Collection<SCSHex> list) {
		float total = 0;
		for (SCSHex hex : list) {
			int attack = 0;
			for (SCSCounter c : hex.getUnits()) {
				attack += c.getAttack();
			}
			if(hex.hasDG()) {
				attack /= 2;
			}
			total += attack;
		}
		return total;
	}

	public static int[] calculateOdds(SCSHex target, Collection<SCSHex> attacking, Hex targetPosition) {
		Collection<SCSCounter> defending = target.getUnits();
		float defence = getDefenceRawSum(defending);
		float defenceModifier = target.getDefenceCombatModifier();
		defence *= defenceModifier;
		float attack = getAttackRawSum(attacking);
		float smaller = Math.min(attack, defence);
		if (smaller == 0) {
			throw new EarlException("Error calculating odds: " + defence + ":" + attack);
		}
		int a = Math.round(attack / smaller);
		int b = Math.round(defence / smaller);
		int[] odds = { a, b };
		return odds;
	}

}
