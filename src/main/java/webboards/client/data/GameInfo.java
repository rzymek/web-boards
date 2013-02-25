package webboards.client.data;

import java.io.Serializable;
import java.util.List;

import webboards.client.games.scs.bastogne.BastogneSide;
import webboards.client.ops.Operation;

import com.google.gwt.user.client.rpc.IsSerializable;

public class GameInfo implements Serializable, IsSerializable {
	private static final long serialVersionUID = 2L;
	public GameFactory game;
	public String channelToken;
	public List<Operation> ops;
	public BastogneSide joinAs;
	public BastogneSide side;
}
