package earl.server;

import java.io.Serializable;

public class CacheEntry implements Serializable {
	public String nextKey;
	public OperationEntity value;
}
