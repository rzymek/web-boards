package earl.client.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;


public class Hex implements Serializable, Identifiable {
	protected Collection<Counter> pieces = new ArrayList<Counter>();
	private String hexId = null;
	
	protected Hex() {
	}
	
	public Hex(String hexId) {
		this.hexId = hexId;
	}

	public Collection<Counter> getStack() {
		return Collections.unmodifiableCollection(pieces);
	}

	@Override
	public String getId() {
		return hexId;
	}
}
