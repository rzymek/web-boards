package webboards.client.data;

import java.io.Serializable;

import webboards.client.games.Scenario;


public interface Game extends Serializable {
	Side[] getSides();

	boolean isParticipating(String user);

	String[] getPlayers();

	Board start(Scenario scenario);
}
