package earl.client.display;

import java.util.Collection;

import earl.client.data.Counter;
import earl.client.data.Identifiable;
import earl.client.games.Ref;

public interface EarlDisplay {

	VisualCoords getCenter(Ref to);

	void drawArrow(VisualCoords start, VisualCoords end, String id);

	void update(Identifiable counter, String state);

	void alignStack(Ref ref);

	void drawOds(VisualCoords center, int[] odds);

	void mark(Collection<? extends Ref> hexes);

	void clearMarks();

	void clearOds(VisualCoords center);

	void clearArrow(Ref from);

	void showResults(VisualCoords center, String result);

	void clearResults(VisualCoords center);

	void drawArrow(Ref from, Ref to, String id);

	void drawLine(Ref fromRef, Ref toRef);

	void select(Counter i);

}
