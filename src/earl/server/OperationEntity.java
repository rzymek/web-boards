package earl.server;

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
	//class name
	public String type;
}
