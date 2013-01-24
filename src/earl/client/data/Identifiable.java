package earl.client.data;



public abstract class Identifiable {
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
		return getClass().getName()+"@"+getId();
	}
}
