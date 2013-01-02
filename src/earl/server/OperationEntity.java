package earl.server;

import java.util.Arrays;
import java.util.Date;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
@Cache
public class OperationEntity {
	@Id	
	public Long id;
	@Index
	public String sessionId;
	public String data;
	public String className;
	@Index
	public Date timestamp;
	
	@SuppressWarnings("unchecked")
	@Override
	public String toString() {
		return Arrays.asList(id,sessionId,data,className).toString();
	}
}

