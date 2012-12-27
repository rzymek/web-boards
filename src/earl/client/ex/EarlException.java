package earl.client.ex;


public class EarlException extends RuntimeException {

	public EarlException(Exception e) {
		super(e);
	}

	public EarlException(String string) {
		super(string);
	}

}
