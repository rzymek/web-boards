package webboards.client.ex;

import java.io.Serializable;

public class EarlException extends RuntimeException implements Serializable {

	private static final long serialVersionUID = 1L;

	protected EarlException() {}
	public EarlException(Exception e) {
		super(e);
	}

	public EarlException(String string) {
		super(string);
	}

}
