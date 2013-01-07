package earl.client.op;

import java.util.Collection;

import earl.client.bastogne.op.CombatResult;
import earl.client.data.Hex;
import earl.client.data.Identifiable;

public interface EarlDisplay {

	Position getCenter(Identifiable to);

	void drawArrow(Position start, Position end, Hex from);

	void update(Identifiable counter, String state);

	void alignStack(Hex hex);

	void select(Identifiable i);

	void drawOds(Position center, int[] odds);

	void mark(Collection<Hex> hexes);

	void clearMarks();

	void clearOds(Position center);

	void clearArrow(Hex from);

	void showResults(Position center, String result);

}
