package earl.server.ex;

public class EarlServerException extends RuntimeException {

	public EarlServerException(String string) {
		super(string);
	}

	public EarlServerException(Exception e) {
		super(e);
	}

}