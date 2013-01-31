package earl.client.display;

import java.util.Collection;

import earl.client.data.Hex;
import earl.client.data.Identifiable;
import earl.client.data.ref.HexRef;

public interface EarlDisplay {

	Position getCenter(Identifiable to);

	void drawArrow(Position start, Position end, String id);

	void update(Identifiable counter, String state);

	void alignStack(Hex hex);

	void select(Identifiable i);

	void drawOds(Position center, int[] odds);

	void mark(Collection<Hex> hexes);

	void clearMarks();

	void clearOds(Position center);

	void clearArrow(Hex from);

	void showResults(Position center, String result);

	void clearResults(Position center);

	void drawArrow(Identifiable from, Identifiable to, String id);

	void drawLine(Identifiable fromRef, Identifiable toRef);

}
