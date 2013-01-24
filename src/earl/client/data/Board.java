package earl.client.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import earl.client.bastogne.op.CounterRef;
import earl.client.bastogne.op.HexRef;
import earl.client.utils.Browser;

public class Board implements Serializable {
	private Map<String, Counter> counters = null;
	private final Map<String, Hex> hexes = new HashMap<String, Hex>();;

	public Board() {
		counters = new HashMap<String, Counter>();
	}

	public void debug() {
		cleanupHexes();
		Counter[] c = counters.values().toArray(new Counter[0]);
		Hex[] h = hexes.values().toArray(new Hex[0]);
		Browser.console(c);
		Browser.console(h);
	}

	private void cleanupHexes() {
		List<String> empty = new ArrayList<String>();
		for (Hex hex : hexes.values()) {
			if (hex.pieces.isEmpty()) {
				empty.add(hex.getId());
			}
		}
		for (String string : empty) {
			hexes.remove(string);
		}
	}

	public Collection<Hex> getStacks() {
		Set<Hex> stacks = new HashSet<Hex>();
		Set<Entry<String, Counter>> entrySet = counters.entrySet();
		for (Entry<String, Counter> entry : entrySet) {
			stacks.add(entry.getValue().getPosition());
		}
		return stacks;
	}

	public Collection<Counter> getCounters() {
		return counters.values();
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
		Counter prev = counters.put(counter.getId(), counter);
		if (prev != null) {
			throw new RuntimeException(counter.getId() + " aleader placed");
		}
	}

	public List<Hex> getAdjacent(Hex position) {
		String id = position.getId();
		return getAdjacent(id);
	}

	public List<Hex> getAdjacent(String id) {
		int x = Integer.parseInt(id.substring(1, 3));
		int y = Integer.parseInt(id.substring(3, 5));
		List<Hex> adj = new ArrayList<Hex>(6);
		int o = (x % 2 == 0) ? 0 : -1;
		//@formatter:off
								adj.add(toId(x,y+1));
		adj.add(toId(x-1,y+1+o));					adj.add(toId(x+1,y+1+o));	
		adj.add(toId(x-1,y+o));						adj.add(toId(x+1,y+o));
								adj.add(toId(x,y-1));
		//@formatter:on
		return adj;
	}

	private Hex toId(int x, int y) {		
		return getHex("h"+dig(x)+""+dig(y));
	}
	
	private static String dig(int x){
		return x<10 ? "0"+x : ""+x;
	}

	public Counter get(CounterRef ref) {
		return getCounter(ref.getId());
	}

	public Hex get(HexRef ref) {
		return getHex(ref.getId());
	}

}
