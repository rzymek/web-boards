package earl.client.display;

import java.util.Map;

import earl.client.data.Board;
import earl.client.games.SCSCounter;

public interface Display {

	void init(Board board);

	void addGameListener(GameChangeListener listener);

	void toggleUnits();

	DisplayHandler getDisplayHandler();

	void showAttacks(Map<SCSCounter, SCSCounter> attacks);

}
