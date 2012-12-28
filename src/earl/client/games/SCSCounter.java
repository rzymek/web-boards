package earl.client.games;

import java.io.Serializable;

import earl.client.data.Counter;

public class SCSCounter extends Counter implements Serializable {
	private String description;
	private String frontPath = null;
	private String id;
	private String back;
	private BastogneSide owner;
	private int attack;
	private Integer range;
	private int defence;
	private int movement;

	protected SCSCounter() {
	}

	public SCSCounter(String id, String frontPath, String back, BastogneSide owner,int attack, Integer range, int defence, int movement) {
		this.id = id;
		this.frontPath = frontPath;
		this.back = back;
		this.owner = owner;
		this.attack = attack;
		this.range = range;
		this.defence = defence;
		this.movement = movement;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getState() {
		return flipped ? back : frontPath;
	}
	
	public BastogneSide getOwner() {
		return owner;
	}
	
	public int getAttack() {
		return attack;
	}

	public int getRange() {
		return range;
	}

	public int getDefence() {
		return defence;
	}

	public int getMovement() {
		return movement;
	}

	@Override
	public void flip() {
		if(back != null) {
			flipped = !flipped;
		}
	}
	@Override
	public String toString() {
		String def = range > 0 ?
				"["+attack+"("+range+")"+"-"+defence+"-"+movement+"]" : 
				"["+attack+"-"+defence+"-"+movement+"]";
		return getId() + def;
	}
}
