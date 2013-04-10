package webboards.client.ex;


public class WebBoardsServerException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	protected WebBoardsServerException() {}

	public WebBoardsServerException(String string) {
		super(string);
	}

	public WebBoardsServerException(Exception e) {
		super(e);
	}

}
