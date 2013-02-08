package webboards.client.data;

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

import webboards.client.data.ref.CounterId;
import webboards.client.ex.EarlException;
import webboards.client.games.Hex;
import webboards.client.games.Position;

public abstract class Board implements Serializable {
	private static final long serialVersionUID = 1L;
	private Map<String, CounterInfo> counters = null;

	public Board() {
		counters = new HashMap<String, CounterInfo>();
	}

	public Set<Position> getStacks() {
		Set<Position> stacks = new HashSet<Position>();
		Set<Entry<String, CounterInfo>> entrySet = counters.entrySet();		
		for (Entry<String, CounterInfo> entry : entrySet) {
			stacks.add(entry.getValue().getPosition());
		}
		return stacks;
	}

	public Collection<CounterInfo> getCounters() {
		return Collections.unmodifiableCollection(counters.values());
	}

	public CounterInfo getCounter(String id) {
		return counters.get(id);
	}

	public void place(Position to, CounterInfo counter) {
		String id = counter.ref().toString();
		CounterInfo prev = counters.put(id, counter);
		if (prev != null) {
			throw new EarlException(id + " aleader placed");
		}
		move(to, counter);
	}

	public void move(Position to, CounterInfo counter) {
		Position from = counter.getPosition();
		if(from != null) {
			getInfo(from).pieces.remove(counter);
		}
		counter.setPosition(to);
		getInfo(to).pieces.add(counter);
	}

	public List<HexInfo> getAdjacent(Hex p) {
		List<HexInfo> adj = new ArrayList<HexInfo>(6);
		int o = (p.x % 2 == 0) ? 0 : -1;
		//@formatter:off
								adj.add(toId(p.x,p.y+1));
		adj.add(toId(p.x-1,p.y+1+o));					adj.add(toId(p.x+1,p.y+1+o));	
		adj.add(toId(p.x-1,p.y+o));						adj.add(toId(p.x+1,p.y+o));
								adj.add(toId(p.x,p.y-1));
		//@formatter:on
		return adj;
	}
	
	private HexInfo toId(int x, int y) {
		return getInfo(new Hex(x, y));
	}

	public abstract HexInfo getInfo(Position area);

	public CounterInfo getInfo(CounterId ref) {
		return getCounter(ref.toString());
	}

}
