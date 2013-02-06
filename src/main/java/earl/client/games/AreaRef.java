package earl.client.games;

public class AreaRef implements Ref {
	private static final long serialVersionUID = 1L;
	private String id;

	protected AreaRef() {}
	public AreaRef(String id) {
		this.id = id;		
	}
	@Override
	public String getSVGId() {
		return id;
	}

}
