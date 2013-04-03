package webboards.client.ex;

import java.io.Serializable;

public class WebBoardsException extends RuntimeException implements Serializable {

	private static final long serialVersionUID = 1L;

	protected WebBoardsException() {}
	public WebBoardsException(Exception e) {
		super(e);
	}

	public WebBoardsException(String string) {
		super(string);
	}

}
