package earl.client.data;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

import earl.client.games.Position;
import earl.client.games.scs.bastogne.BastogneSide;
import earl.client.ops.Operation;

public class GameInfo implements Serializable, IsSerializable {
	private static final long serialVersionUID = 1L;
	public Game game;
	public String channelToken;
	public Collection<Operation> ops;
	public BastogneSide joinAs;
	public BastogneSide side;
	public Map<String, Position> state;
}
