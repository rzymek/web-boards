package earl.client.display;

import java.util.List;

import earl.client.data.Counter;
import earl.client.data.Hex;

public interface DisplayChangeListener extends GameChangeListener {

	void counterDeselected(Counter selectedPiece);

	void counterSelected(Counter selectedPiece);

	void showStackSelection(Hex hex);

	void alignStack(Hex position);

	boolean areCountersOverlapping(Hex hex, List<Counter> stack);

}
