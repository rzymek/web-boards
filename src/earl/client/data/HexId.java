package earl.client.data;

import earl.client.bastogne.op.HexRef;

public class HexId implements HexRef {
	private static final long serialVersionUID = 1L;
	private String id;
	public HexId() {		
	}
	public HexId(String id) {
		this.id = id;		
	}
	@Override
	public String getId() {
		return id;
	}

}
