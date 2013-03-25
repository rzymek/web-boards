package webboards.server.entity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;

@Entity
@Cache
@SuppressWarnings("unused")
public class TableSearch {
	@Id
	public long id;
	@Index
	private Key<Table> table;
	@Index @Load
	private List<String> players = new ArrayList<String>();
	@Index
	private boolean full = false;
	public Date started = new Date();
	public String info;

	private TableSearch(){}
	
	public TableSearch(Table table) {
		this.table = Key.create(table);
		this.id = table.id;
		this.info = table.game.getClass().getSimpleName() + " - " + table.scenario.getClass().getSimpleName();
	}

	public void join(Player player, int maxPlayers) {
		players.add(player.user);
		full = (players.size() >= maxPlayers); 
	}

	@Override
	public String toString() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd HH:MM");
		return table.getId() + ": " + info + " with "+players+" (" + format.format(started) + ")";
	}
}
