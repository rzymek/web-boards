package earl.client.ex;


public class EarlException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EarlException(Exception e) {
		super(e);
	}

	public EarlException(String string) {
		super(string);
	}

}
