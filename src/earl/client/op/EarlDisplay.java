package earl.client.op;

import earl.client.data.Hex;
import earl.client.data.Identifiable;

public interface EarlDisplay {

	Position getCenter(Identifiable to);

	void setCenter(Identifiable counter, Position dest);

	void drawArrow(Position start, Position end, String id);

	void update(Identifiable counter, String state);

	void alignStack(Hex hex);

	void select(Identifiable i);

	void drawOds(Position center, int[] odds);

}
