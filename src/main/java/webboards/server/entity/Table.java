package webboards.server.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import webboards.client.data.Game;
import webboards.client.games.scs.bastogne.BastogneSide;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Serialize;

@Entity
@Cache
public class Table implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id	
	public Long id;
	public Date started = new Date();
	@Index
	public String player1;
	@Index
	public String player2;
	
	public BastogneSide last = null;
	@Serialize
	public Game state;
	public Date stateTimestamp;

	@Override
	public String toString() {
		String start = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(started);
		return String.format("US: %s vs GE: %s. Started at %s", norm(player1), norm(player2), start);		
	}

	private static String norm(String player) {
		if(player == null) {
			return "vacant";
		}else{
			return player;
		}
	}
}
