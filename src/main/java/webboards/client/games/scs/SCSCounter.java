package webboards.client.games.scs;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import webboards.client.data.CounterInfo;
import webboards.client.data.GameCtx;
import webboards.client.data.ref.CounterId;
import webboards.client.ex.EarlException;
import webboards.client.games.Position;
import webboards.client.games.scs.bastogne.BastogneSide;
import webboards.client.ops.NotImplemented;
import webboards.client.ops.Operation;

public class SCSCounter extends CounterInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private String description;
	private String frontPath = null;
	private String back;
	private BastogneSide owner;
	private int attack;
	private Integer range;
	private int defence;
	private int movement;

	protected SCSCounter() {
		super(null);
	}

	public SCSCounter(String id, String frontPath, String back, BastogneSide owner,int attack, Integer range, int defence, int movement) {
		super(new CounterId(id));
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
		return ref() + def;
	}

	@Override
	public Operation onPointTo(GameCtx ctx, CounterInfo c) {
		if(c instanceof SCSCounter) {
			SCSCounter counter = (SCSCounter) c;
			if(counter.getOwner() == owner) {
				return super.onPointTo(ctx, c);
			}else{
				return onPointToStack(ctx, Arrays.asList(c), c.getPosition());
			}
		}else{
			return super.onPointTo(ctx, c);
		}
	}
	
	@Override
	public Operation onPointToStack(GameCtx ctx, List<CounterInfo> stack, Position pos) {
		BastogneSide side = getSide(stack);
		if(side == null) {
			//no counters, only markers
			return super.onPointToStack(ctx, stack, pos);
		}
		if(side != owner) {			
			//attack
			if(isRanged()) {
				return onBarrage(ctx, stack, pos);
			}else{
				if(SCSHex.isAdjacent(pos, getPosition())){
					return onCombat(ctx, stack, pos);
				}else{
					ctx.display.select(null);
					return null;
				}
			}
		}else{
			return super.onPointToStack(ctx, stack, pos);
		}
	}

	private Operation onCombat(GameCtx ctx, List<CounterInfo> stack, Position pos) {
		return new NotImplemented("combat");
	}

	private Operation onBarrage(GameCtx ctx, List<CounterInfo> stack, Position pos) {
		return new NotImplemented("barrage");
	}

	private BastogneSide getSide(List<CounterInfo> stack) {
		BastogneSide side = null;
		for (CounterInfo c : stack) {
			if(c instanceof SCSCounter) {
				SCSCounter counter = (SCSCounter) c;
				if(side != null && side != counter.getOwner()) {
					throw new EarlException("Inconsistent board: Mixed side in one stack");
				}
				side = counter.getOwner();
			}
		}
		return side;
	}
}
