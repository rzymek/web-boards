package webboards.client.games.scs;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import webboards.client.data.CounterInfo;
import webboards.client.data.GameCtx;
import webboards.client.data.ref.CounterId;
import webboards.client.display.Color;
import webboards.client.ex.EarlException;
import webboards.client.games.Hex;
import webboards.client.games.Position;
import webboards.client.games.scs.bastogne.BastogneSide;
import webboards.client.games.scs.ops.PerformBarrage;
import webboards.client.ops.Operation;

public class SCSCounter extends CounterInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	protected String description;
	private String frontPath = null;
	private String back;
	private BastogneSide owner;
	private int attack;
	private Integer range;
	private int defence;
	private int movement;

	private SCSCounter() {
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

	@Override
	public String getState() {
		return flipped ? back : frontPath;
	}
	
	public BastogneSide getOwner() {
		return owner;
	}
	
	public boolean isRanged() {
		return range != null;
	}

	@Override
	public void flip() {
		if(back != null) {
			flipped = !flipped;
		}
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
			// no counters, only markers
			return super.onPointToStack(ctx, stack, pos);
		}
		if (!(pos instanceof Hex)) {
			return null;
		}
		if (side != owner) {
			ctx.display.select(null);
			// attack
			Hex hex = (Hex) pos;
			if (isRanged()) {
				return onBarrage(ctx, stack, hex);
			} else {
				if (SCSHex.isAdjacent(pos, getPosition())) {
					return onCombat(ctx, stack, hex);
				} else {
					// trying to attack non-adjacent enemy
					return null;
				}
			}
		}else{
			return super.onPointToStack(ctx, stack, pos);
		}
	}
	
	public static int[] calculateOdds(SCSHex target, Collection<SCSHex> attacking, Hex targetPosition) {
		List<CounterInfo> defending = target.getPieces();
		float defence = getDefenceRawSum(defending);
		float defenceModifier = target.getDefenceCombatModifier();
		defence *= defenceModifier;			
		float attack = getAttackRawSum(attacking);
		float smaller = Math.min(attack, defence);
		int a = Math.round(defence / smaller);
		int b = Math.round(attack / smaller);
		int[] odds = { a, b };
		return odds;
	}

	private Operation onCombat(GameCtx ctx, List<CounterInfo> stack, Hex target) {
		SCSBoard board = (SCSBoard) ctx.board;
		Hex from = (Hex) getPosition();
		board.declareAttack(from, target);
		
		ctx.display.drawArrow(from, target, "combat_"+from.getSVGId(), SCSColor.DELCARE.getColor());
		
		SCSHex targetHex = (SCSHex) ctx.board.getInfo(target);
		int[] odds = calculateOdds(targetHex, board.getAttackingInfo(target), target);
		String text = odds[0] + ":" + odds[1];
		ctx.display.drawOds(ctx.display.getCenter(target), text, target.getSVGId());
		return null;
	}

	private Operation onBarrage(GameCtx ctx, List<CounterInfo> stack, Hex target) {
		SCSBoard board = (SCSBoard) ctx.board;
		board.declareBarrage(this, target);
		new PerformBarrage(this, target).drawDetails(ctx);
		SCSHex hex = (SCSHex) ctx.board.getInfo(target);
		int value = attack + hex.getBarrageModifier();
		ctx.display.drawOds(ctx.display.getCenter(target), "" + value, ref().toString());
		return null;
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
	
	private static  float getDefenceRawSum(Collection<CounterInfo> defending) {
		float defence = 0;
		for (CounterInfo counter : defending) {
			defence += ((SCSCounter) counter).defence;
		}
		return defence;
	}

	private static float getAttackRawSum(Collection<SCSHex> list) {
		float attack = 0;
		for (SCSHex hex : list) {
			for (CounterInfo c : hex.getPieces()) {
				//TODO: !instanceof SCSMarker!
				attack += ((SCSCounter) c).attack;
			}
		}
		return attack;
	}
	
	@Override
	public String toString() {
		String def = range != null ?
				"["+attack+"("+range+")"+"-"+defence+"-"+movement+"]" : 
				"["+attack+"-"+defence+"-"+movement+"]";
		return ref() + def;
	}

	public int getAttack() {
		return attack;
	}
}
