package earl.engine.client.data;

import java.io.Serializable;
import java.util.Map;

public class GameInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	public String channelToken;
	public Map<String, String> units;
	public Map<String, String> sides;
}
