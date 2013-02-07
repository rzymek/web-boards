package earl.client.data.ref;

import java.io.Serializable;

import earl.client.utils.Utils;

public class CounterId implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;

	@SuppressWarnings("unused")
	private CounterId() {
	}

	public CounterId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return id;
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;			
		}
		if(obj instanceof CounterId) {
			CounterId other = (CounterId) obj;
			return Utils.equals(id, other.id);			
		}else{
			return false;
		}
	}
}
