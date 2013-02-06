package earl.server.entity;

import java.util.Date;
import java.util.Map;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;

import earl.client.games.Ref;

@Entity
@Cache
public class GameState {
	@Parent
	public Key<Table> table;
	@Id
	public String user;
	public Date updated;
	public Map<String, Ref> state;
}
