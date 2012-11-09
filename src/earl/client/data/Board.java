package earl.client.data;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Board implements Serializable {
	private Map<String, Counter> counters = null;
	private Map<String, Hex> hexes = null;

	public Board() {
		counters = new HashMap<String, Counter>();
		hexes = new HashMap<String, Hex>();
	}
	
	public Collection<Hex> getStacks() {
		Set<Hex> stacks = new HashSet<Hex>();
		Set<Entry<String, Counter>> entrySet = counters.entrySet();
		for (Entry<String, Counter> entry : entrySet) {
			stacks.add(entry.getValue().getPosition());
		}
		return stacks;
	}

	public Hex getHex(String hexId) {
		Hex hex = hexes.get(hexId);
		if (hex == null) {
			hexes.put(hexId, hex = new Hex(hexId));
		}
		return hex;
	}

	public Counter getCounter(String id) {
		return counters.get(id);
	}

	public void place(String hexId, Counter counter) {
		Hex hex = getHex(hexId);
		counter.setPosition(hex);
		counters.put(counter.getId(), counter);
	}

}
