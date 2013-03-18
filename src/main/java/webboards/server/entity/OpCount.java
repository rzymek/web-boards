package webboards.server.entity;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class OpCount {
	@Id @SuppressWarnings("unused") 
	private Long id;
	@Parent @SuppressWarnings("unused") 
	private Key<Table> table;

	private int value;

	@SuppressWarnings("unused")
	private OpCount(){}
	public OpCount(Table table) {
		this.table = Key.create(table);
	}

	public void incement() {
		++value;
	}

	public int count() {
		return value;
	}
	@Override
	public String toString() {
		return String.valueOf(value);
	}
}
