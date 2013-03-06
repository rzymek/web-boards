package webboards.server.entity;

import java.io.Serializable;

import webboards.client.data.Side;
import webboards.server.notify.Notify;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;

public class Player implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Parent
	public Key<Table> table;
	@Id
	public Side side;
	public String user;
	public String channelId;
	
	protected Player() {
	}
	
	public Player(Table table, String user, Side side, Notify notify) {
		this.table = Key.create(table);
		this.user = user;
		this.side = side;
		this.channelId = notify.openChannel(table, side);		
	}
}
