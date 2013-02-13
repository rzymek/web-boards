package webboards.client.ops;

import java.io.Serializable;

import webboards.client.data.Board;
import webboards.client.data.GameCtx;

public interface Operation extends Serializable {

	void updateBoard(Board board);

	void drawDetails(GameCtx ctx);

	void serverExecute(ServerContext ctx);

	void postServer(GameCtx ctx);

	@Override
	String toString();

	void draw(GameCtx ctx);
}
