package earl.client.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@SuppressWarnings("serial")
public class Hex extends Identifiable {
	protected List<Counter> pieces = new ArrayList<Counter>();
	private String hexId = null;
	
	protected Hex() {
	}
	
	public Hex(String hexId) {
		this.hexId = hexId;
	}

	public List<Counter> getStack() {
		return Collections.unmodifiableList(pieces);
	}

	@Override
	public String getId() {
		return hexId;
	}
	
	@Override
	public String toString() {
		String s = super.toString() + " {";
		for (Counter c : pieces) {
			s+=c.getId()+", ";
		}
		return s+"}";
	}
}
