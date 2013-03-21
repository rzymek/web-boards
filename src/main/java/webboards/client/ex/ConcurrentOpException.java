package webboards.client.ex;

public class ConcurrentOpException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private ConcurrentOpException() {
	}

	public ConcurrentOpException(String msg) {
		super(msg);
	}

}
