package webboards.server.entity;

import java.io.Serializable;
import java.util.Date;

import webboards.client.data.Game;
import webboards.client.games.Scenario;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Serialize;

@Entity
@Cache
public class Table implements Serializable {
	private static final long serialVersionUID = 3L;
	@Id	
	public Long id;
	public Date started = new Date();
	@Serialize
	public Game game;
	@Serialize
	public Scenario scenario;
	
	public Date stateTimestamp;
	public String lastOp;

	@Override
	public String toString() {
		return String.format("%s: %s %s", id, game.getClass().getSimpleName(), scenario.getClass().getSimpleName());		
	}
}
