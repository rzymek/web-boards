package webboards.server.entity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

import webboards.client.games.scs.bastogne.BastogneSide;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

@Entity
@Cache
public class OperationEntity implements Comparable<OperationEntity>, Serializable {
	private static final long serialVersionUID = 1L;
	@Parent
	public Key<Table> table;
	@Id
	public Long id;
	public byte[] data;
	public String className;
	@Index
	public Date timestamp = new Date();
	public String adebug;
	public BastogneSide author;

	@Override
	public String toString() {
		return Arrays.asList(id, data, className).toString();
	}

	@Override
	public int compareTo(OperationEntity o) {
		return timestamp.compareTo(o.timestamp);
	}
}
