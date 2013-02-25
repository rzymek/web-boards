package webboards.client.games.scs.bastogne;

import webboards.client.data.Game;
import webboards.client.data.GameFactory;

public class BastogneFactory implements GameFactory {
	private static final long serialVersionUID = 1L;

	@Override
	public Game start() {
		Bastogne game = new Bastogne();
		game.setupScenarion52();
		return game;
	}

}
