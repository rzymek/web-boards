package webboards.server.entity;

import java.util.Date;

import webboards.client.data.Game;
import webboards.client.games.scs.bastogne.BastogneSide;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Serialize;

@Entity
@Cache
public class CurrentGame {
	@Id	
	public Long id;
	public BastogneSide last = null;
	@Serialize
	public Game state;
	public Date stateTimestamp;
}
