package webboards.client.data;

import java.io.Serializable;
import java.util.List;

import webboards.client.games.Scenario;
import webboards.client.ops.Operation;

/**
 * DTO used to transfer all the game information from the server
 * to the client. 
 */
public class GameInfo implements Serializable {
	private static final long serialVersionUID = 3L;
	public Game game;
	public Scenario scenario;
	public String channelToken;
	public List<Operation> ops;
	public Side joinAs;
	public Side side;
}
