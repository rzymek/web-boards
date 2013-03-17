package webboards.client.ops.generic;

import java.util.Random;

import webboards.client.ops.Operation;
import webboards.client.ops.ServerContext;

public class DiceRoll extends Operation {
	private static final long serialVersionUID = 1L;
	public int dice = 2;
	public int sides = 6;
	private int[] results;

	private transient Random random;

	@Override
	public void serverExecute(ServerContext ctx) {
		results = rollDice(dice, sides);
	}

	private int[] rollDice(int d, int sides) {
		if (random == null) {
			random = new Random();
		}
		int[] dice = new int[d];
		for (int i = 0; i < d; ++i) {
			int die = random.nextInt(sides) + 1;
			dice[i] = die;
		}
		return dice;
	}

	private int getSum(int[] dice) {
		int sum = 0;
		for (int d : dice) {
			sum += d;
		}
		return sum;
	}

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();		
		if(dice > 1) {
			buf.append(dice);
		}
		buf.append('d').append(sides).append(": ");
		buf.append(getSum()).append(" (");
		for (int i = 0; i < results.length; i++) {
			if(i>0) {
				buf.append(", ");			
			}
			buf.append(results[i]);
		}
		buf.append(")");
		return  buf.toString();
	}

	public int getSum() {
		return getSum(results);
	}
}
