package webboards.client.games.scs;

import webboards.client.games.Hex;

public class Attack {
	
	public final Type type;
	public final Hex target;

	public Attack(Hex defending, Type type) {
		this.target = defending;
		this.type = type;
	}

	public static enum Type {
		Combat,Barrage
	}

	public boolean isTarget(Hex target) {
		return this.target.equals(target);
	}
}
