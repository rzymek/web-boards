package webboards.client.display;

import java.util.Collection;
import java.util.List;

import webboards.client.data.Board;
import webboards.client.data.CounterInfo;
import webboards.client.data.ref.CounterId;
import webboards.client.games.Position;

public interface Display {
	public static enum Mode {
		VIEW_ONLY, INTERACTIVE
	}
	
	void drawArrow(Position from, Position to, String id, Color color);
	void drawLine(Position fromRef, Position toRef);
	void smark(Collection<? extends Position> hexes);
	void markUsed(CounterId ref);
	void showResults(VisualCoords center, String result);
	void drawOds(VisualCoords center, String text, String id);
	
	void select(CounterInfo i);
	void showStackSelector(List<CounterInfo> stack, Position pos);

	void clearMarks();
	void clearArrow(String id);
	void clearResults(VisualCoords center);
	void clearTraces();
	void clearOds(String id);
	void clearLine(Position from, Position to);

	VisualCoords getCenter(Position to);
	
	void createCounter(CounterInfo counter, Board board);
	void alignStack(Position ref);
	void update(CounterId counter, String state);
	
	void setText(String id, String value);
	void setMode(Mode mode);
}
