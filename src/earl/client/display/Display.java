package earl.client.display;

import java.util.Map;

import earl.client.data.Board;
import earl.client.data.Hex;
import earl.client.games.Bastogne;

public interface Display {

	void init(Board board);

	void toggleUnits();

	DisplayHandler getDisplayHandler();

	void showAttacks(Map<Hex, Hex> attacks, Bastogne game);

}
