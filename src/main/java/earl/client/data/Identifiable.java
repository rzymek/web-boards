package earl.client.data;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public abstract class Identifiable implements Serializable, IsSerializable {
	private static final long serialVersionUID = 1L;
	public abstract String getId();

	@Override
	public int hashCode() {
		return getId().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof Identifiable) {
			Identifiable o = (Identifiable) obj;
			return this.getId().equals(o.getId());
		} else {
			return false;
		}
	}
	@Override
	public String toString() {
		return getId();
	}
}
