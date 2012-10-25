package earl.engine.client.data;

import java.io.Serializable;
import java.util.Map;

public class GameInfo implements Serializable {
	public String channelToken;
	public Map<String, String> units;
}
