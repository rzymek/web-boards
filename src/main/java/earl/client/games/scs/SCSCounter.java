package earl.client.games.scs;

import java.io.Serializable;
import java.util.List;

import earl.client.data.Board;
import earl.client.data.CounterInfo;
import earl.client.games.Hex;
import earl.client.games.Position;
import earl.client.games.scs.bastogne.BastogneSide;
import earl.client.games.scs.ops.Move;
import earl.client.ops.Operation;

public class SCSCounter extends CounterInfo implements Serializable {
	private static final long serialVersionUID = 1L;
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

	public Integer getRange() {
		return range;
	}
	
	public boolean isRanged() {
		return range != null;
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
		String def = range != null ?
				"["+attack+"("+range+")"+"-"+defence+"-"+movement+"]" : 
				"["+attack+"-"+defence+"-"+movement+"]";
		return getId() + def;
	}
	
	public Operation onPointTo(Board board, Position ref) {
		if(ref instanceof Hex) {
			Hex hex = (Hex) ref;
			if(isEnemyOccupied(board, hex)) {
				return onAttack((SCSHex) board.getInfo(hex));
			}else{
				return onMoveTo(hex);
			}
		}else{
			//e.g. move to Dead pool
			return onMoveTo(ref);			
		}
	}
			
	private Operation onAttack(SCSHex hex) {
		if(hex.isAdjacent(hex)) {
			return onCombat(hex);
		}else if(isRanged()){
			return onBarrage(hex);
		}else{
			return null;
		}	
	}

	private Operation onBarrage(SCSHex hex) {
		return null;
	}

	private Operation onCombat(SCSHex hex) {
		return null;
	}

	private boolean isEnemyOccupied(Board board, Hex hex) {
		List<CounterInfo> stack = board.getInfo(hex).getStack();
		for (CounterInfo counter : stack) {
			if(counter instanceof SCSCounter) {
				SCSCounter c = (SCSCounter) counter;
				//No need to check other counters on the stack
				//Game rules forbid placing counters of different side in one hex. 
				return (c.getOwner() != owner);
			}
		}
		return false;
	}

	protected Operation onMoveTo(Position hex) {
		return new Move(this, hex);
	}

	public void onOpponentClicked() {
		
	}
	
	public void onOwnerSelected() {
		
	}
	
}
