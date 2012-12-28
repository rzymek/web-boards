package earl.manager;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
@Cache
public class Table {
	@Id	
	public Long id;
	public Date started = new Date();
	@Index
	public String player1;
	@Index
	public String player2;
	
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
