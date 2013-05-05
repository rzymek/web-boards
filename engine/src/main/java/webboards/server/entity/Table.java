package webboards.server.entity;

import java.io.Serializable;

import webboards.client.data.Game;
import webboards.client.games.Scenario;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Serialize;

/**
 * Parent entity for Player and OperationEntity. 
 * This is the root entity for a started game. 
 */
@Entity
@Cache
public class Table implements Serializable {
	private static final long serialVersionUID = 3L;
	@Id
	public Long id;
	@Serialize
	public Game game;
	@Serialize
	public Scenario scenario;

	@Override
	public String toString() {
		return String.format("%s: %s - %s", 
				id, 
				game == null ? "?" : game.getClass().getSimpleName(), 
				scenario == null ? "?" : scenario.getClass().getSimpleName());
	}
}
