package earl.server;

import java.io.Serializable;

import earl.server.entity.OperationEntity;

public class CacheEntry implements Serializable {
	private static final long serialVersionUID = 1L;
	public String nextKey;
	public OperationEntity value;
}
