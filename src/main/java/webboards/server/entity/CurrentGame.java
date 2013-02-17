package webboards.server.entity;

import webboards.client.data.Game;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Serialize;

@Entity
@Cache
public class CurrentGame {
	@Id	
	public Long id;
	@Serialize
	public Game state;
	public String lastOp;
}
