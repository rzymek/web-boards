package earl.server.utils;

import earl.client.ex.EarlServerException;

public class Utils {

	@SuppressWarnings("unchecked")
	public static <T> T newInstance(String className) {
		try {
			return (T) Class.forName(className).newInstance();
		} catch (Exception e) {
			throw new EarlServerException(e);
		}
	}

}
