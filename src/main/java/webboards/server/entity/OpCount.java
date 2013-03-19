package webboards.server.entity;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;

@Entity
public class OpCount {
	@SuppressWarnings("unused")
	private Long id;
	@SuppressWarnings("unused")
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
