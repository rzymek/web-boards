package webboards.server.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.NoSuchElementException;

import webboards.client.data.Game;
import webboards.client.games.Scenario;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Serialize;

@Entity
@Cache
public class Table implements Serializable {
	private static final long serialVersionUID = 3L;
	@Id	
	public Long id;
	public Date started = new Date();
	@Index
	public String[] players = new String[2];
	@Serialize
	public Game game;
	@Serialize
	public Scenario scenario;
	
	public Date stateTimestamp;
	public String lastOp;

	@Override
	public String toString() {
		String start = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(started);
		return String.format("US: %s vs GE: %s. Started at %s", norm(players[0]), norm(players[1]), start);		
	}

	private static String norm(String player) {
		if(player == null) {
			return "vacant";
		}else{
			return player;
		}
	}

	public int getPlayerPosition(String user) throws NoSuchElementException {
		for (int i = 0; i < players.length; i++) {
			if(user.equals(players[i]))
				return i;
		}
		throw new NoSuchElementException(user);
	}

	public int getEmptyPosition() {
		try {
			return getPlayerPosition(null);
		}catch(NoSuchElementException ex){
			throw new NoSuchElementException("Free position");
		}
	}
}
