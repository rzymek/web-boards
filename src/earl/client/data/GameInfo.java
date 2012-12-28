package earl.client.data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import earl.client.op.OpData;

public class GameInfo implements Serializable {
	public String channelToken;
	public List<OpData> ops;
	public Map<String, String> mapInfo;
	public String joinAs;
}
