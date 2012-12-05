package earl.client.bastogne.op;

import java.util.Random;

import earl.client.data.Board;
import earl.client.op.Operation;

public class DiceRoll extends Operation {
	public int dice = 2;
	public int sides = 6;
	private int[] results;

	private transient Random random;

	@Override
	public void serverExecute() {
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
	public String encode() {
		StringBuilder s = new StringBuilder();
		s.append(dice).append(':').append(sides);
		for (int r : results) {
			s.append(':').append(r);
		}
		return s.toString();
	}

	@Override
	public void decode(Board board, String s) {
		String[] data = s.split(":");
		dice = Integer.parseInt(data[0]);
		sides = Integer.parseInt(data[1]);
		results = new int[data.length - 2];
		for (int i = 2; i < data.length; i++) {
			results[i-2] = Integer.parseInt(data[i]);
		}
	}

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();		
		if(dice > 1) {
			buf.append(dice);
		}
		buf.append('d').append(sides).append(": ");
		buf.append(getSum(results)).append(" (");
		for (int i = 0; i < results.length; i++) {
			if(i>0) {
				buf.append(", ");			
			}
			buf.append(results[i]);
		}
		buf.append(")");
		return  buf.toString();
	}
}
