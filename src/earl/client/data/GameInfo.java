package earl.client.data;

import java.io.Serializable;

import earl.client.games.Game;

public class GameInfo implements Serializable {
	public Game game;
	public String channelToken;
}
