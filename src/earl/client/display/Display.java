package earl.client.display;

import earl.client.data.Board;

public interface Display {

	public abstract void init(Board board);

	public abstract void addGameListener(GameChangeListener listener);

}
