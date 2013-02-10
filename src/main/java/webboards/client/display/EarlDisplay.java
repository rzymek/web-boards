package webboards.client.display;

import java.util.Collection;
import java.util.List;

import webboards.client.data.Board;
import webboards.client.data.CounterInfo;
import webboards.client.data.ref.CounterId;
import webboards.client.games.Position;

public interface EarlDisplay {

	VisualCoords getCenter(Position to);

	void drawArrow(VisualCoords start, VisualCoords end, String id);

	void update(CounterId counter, String state);

	void alignStack(Position ref);

	void mark(Collection<? extends Position> hexes);

	void clearMarks();

	void clearArrow(Position from);

	void showResults(VisualCoords center, String result);

	void clearResults(VisualCoords center);

	void drawArrow(Position from, Position to, String id);

	void drawLine(Position fromRef, Position toRef);

	void select(CounterInfo i);

	void clearTraces();

	void showStackSelector(List<CounterInfo> stack, Position pos);

	void drawOds(VisualCoords center, String text, String id);

	void createCounter(CounterInfo counter, Board board);

	void clearOds(String id);

}
