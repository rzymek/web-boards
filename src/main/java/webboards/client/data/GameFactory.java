package webboards.client.data;

import java.io.Serializable;

public interface GameFactory extends Serializable {
	public Game start();
}
