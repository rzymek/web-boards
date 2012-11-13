package earl.client.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


@SuppressWarnings("serial")
public class Hex implements Serializable, Identifiable {
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
}
