package earl.client.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import earl.client.data.ref.CounterRef;
import earl.client.games.HexXY;
import earl.client.games.Ref;

public abstract class Board implements Serializable {
	private static final long serialVersionUID = 1L;
	private Map<String, Counter> counters = null;

	public Board() {
		counters = new HashMap<String, Counter>();
	}

	public Set<Ref> getStacks() {
		Set<Ref> stacks = new HashSet<Ref>();
		Set<Entry<String, Counter>> entrySet = counters.entrySet();		
		for (Entry<String, Counter> entry : entrySet) {
			stacks.add(entry.getValue().getPosition());
		}
		return stacks;
	}

	public Collection<Counter> getCounters() {
		return Collections.unmodifiableCollection(counters.values());
	}

	public abstract Hex get(Ref area);

	public Counter getCounter(String id) {
		return counters.get(id);
	}

	public void place(Ref to, Counter counter) {
		Ref from = counter.getPosition();
		if(from != null) {
			get(from).pieces.remove(counter);
		}
		counter.setPosition(to);
		get(to).pieces.add(counter);
		
		Counter prev = counters.put(counter.getId(), counter);
		if (prev != null) {
			throw new RuntimeException(counter.getId() + " aleader placed");
		}
	}

	public List<Hex> getAdjacent(HexXY p) {
		List<Hex> adj = new ArrayList<Hex>(6);
		int o = (p.x % 2 == 0) ? 0 : -1;
		//@formatter:off
								adj.add(toId(p.x,p.y+1));
		adj.add(toId(p.x-1,p.y+1+o));					adj.add(toId(p.x+1,p.y+1+o));	
		adj.add(toId(p.x-1,p.y+o));						adj.add(toId(p.x+1,p.y+o));
								adj.add(toId(p.x,p.y-1));
		//@formatter:on
		return adj;
	}
	
	private Hex toId(int x, int y) {
		return get(new HexXY(x, y));
	}

	public Counter get(CounterRef ref) {
		return getCounter(ref.getId());
	}

}
