package earl.client.bastogne.op;

import java.util.Arrays;

import com.google.gwt.user.client.Window;

import earl.client.data.Board;
import earl.client.data.Hex;
import earl.client.games.Bastogne;
import earl.client.op.EarlDisplay;
import earl.client.op.Operation;

public class PerformAttack extends Operation {
	public Hex target;
	public Hex[] attacking;
	public CombatResult result;
	public String rollResult;	

	@Override
	public void draw(EarlDisplay g) {
	}

	@Override
	public void clientExecute() {
	}

	@Override
	public void serverExecute() {
		Bastogne game = (Bastogne) this.game;
		int[] odds = game.calculateOdds(target, Arrays.asList(attacking));
		DiceRoll roll = new DiceRoll();
		roll.dice = 2;
		roll.sides = 6;
		roll.serverExecute();	
		int sum = roll.getSum();
		rollResult = roll.toString();
		result = game.getCombatResult(odds, sum);
	}

	@Override
	public String encode() {
		String att = encode(attacking);
		return encodeObj(target.getId(), result, rollResult, att);
	}

	@Override
	public void decode(Board board, String s) {
		String[] data = s.split(":");
		target = board.getHex(data[0]);
		result = new CombatResult(data[1]);
		rollResult = data[2];
		int listOffset = 3;
		attacking = new Hex[data.length-listOffset];		
		for (int i = listOffset; i < data.length; ++i) {
			attacking[i-listOffset] = board.getHex(data[i]);
		}
	}
	@Override
	public void postServer(EarlDisplay display) {
		Window.alert("Dice roll: "+rollResult+"\n" +
				"Attack result: "+result);
		display.clearOds(display.getCenter(target));
		for (Hex from: attacking) {			
			display.clearArrow(from);
		}
	}
}