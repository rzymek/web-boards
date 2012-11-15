package earl.client.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import earl.client.utils.Browser;

public class Board implements Serializable {
	private Map<String, Counter> counters = null;
	private Map<String, Hex> hexes = new HashMap<String, Hex>();;

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
			if(hex.pieces.isEmpty()) {
				empty.add(hex.getId());
			}
		}
		for (String string : empty) {
			hexes.remove(string);
		}
	}
	
	public List<Hex> getStacks() {
		List<Hex> stacks = new ArrayList<Hex>();
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
//		if(hexes == null) {
//			hexes = new HashMap<String, Hex>();
//			for (Counter counter : counters.values()) {
//				Hex hex = counter.getPosition();
//				Hex existing = hexes.get(hex.getId());
//				if(existing == null){
//					hexes.put(hex.getId(), hex);
//				}else{
//					counter.position = existing;
//				}
//			}
//		}if(hexes == null) {
//		hexes = new HashMap<String, Hex>();
//		for (Counter counter : counters.values()) {
//			Hex hex = counter.getPosition();
//			Hex existing = hexes.get(hex.getId());
//			if(existing == null){
//				hexes.put(hex.getId(), hex);
//			}else{
//				counter.position = existing;
//			}
//		}
//	}
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
		if(prev != null){
			throw new RuntimeException(counter.getId()+" aleader placed");
		}
	}

}
