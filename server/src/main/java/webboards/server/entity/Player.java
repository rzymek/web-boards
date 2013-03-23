package webboards.server.entity;

import java.io.Serializable;

import webboards.client.data.Side;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;
import com.googlecode.objectify.annotation.Serialize;

@Entity
@Cache
public class Player implements Serializable {
	private static final long serialVersionUID = 1L;

	@Parent
	public Key<Table> table;
	@Id
	protected String id;
	@Serialize
	public Side side;
	@Index
	public String user;
	public String channelToken;

	protected Player() {
	}

	public Player(Table table, String user, Side side) {
		this.table = Key.create(table);
		this.user = user;
		this.side = side;
		this.id = side.toString();
	}

	@Override
	public String toString() {
		return side + " " + user;
	}
}
