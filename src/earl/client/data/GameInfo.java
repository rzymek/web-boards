package earl.client.data;

import java.io.Serializable;
import java.util.List;

import earl.client.games.Game;
import earl.client.op.Operation;

public class GameInfo implements Serializable {
	public Game game;
	public String channelToken;
	public String log;
	public List<Operation> ops;
}
