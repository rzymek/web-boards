package webboards.client.ex;


public class EarlServerException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	protected EarlServerException() {}

	public EarlServerException(String string) {
		super(string);
	}

	public EarlServerException(Exception e) {
		super(e);
	}

}
