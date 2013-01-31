package earl.client.data;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import earl.client.games.scs.bastogne.BastogneSide;
import earl.client.ops.Operation;

public class GameInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	public String channelToken;
	public Collection<Operation> ops;
	public Map<String, String> mapInfo;
	public BastogneSide joinAs;
	public BastogneSide side;
}
