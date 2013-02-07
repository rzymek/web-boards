package earl.client.display;

import java.util.Collection;

import earl.client.data.Counter;
import earl.client.data.Identifiable;
import earl.client.games.Ref;

public interface EarlDisplay {

	Position getCenter(Ref to);

	void drawArrow(Position start, Position end, String id);

	void update(Identifiable counter, String state);

	void alignStack(Ref ref);

	void drawOds(Position center, int[] odds);

	void mark(Collection<? extends Ref> hexes);

	void clearMarks();

	void clearOds(Position center);

	void clearArrow(Ref from);

	void showResults(Position center, String result);

	void clearResults(Position center);

	void drawArrow(Ref from, Ref to, String id);

	void drawLine(Ref fromRef, Ref toRef);

	void select(Counter i);

}
