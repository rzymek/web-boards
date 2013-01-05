package earl.client.op;

import java.io.Serializable;

import earl.client.data.Board;
import earl.client.data.Identifiable;
import earl.client.games.Game;

public abstract class Operation implements Serializable {
	public transient Game game;
	
	public void draw(EarlDisplay g) {		
	}

	public void clientExecute(){
	}

	public void drawDetails(EarlDisplay g) {
	}

	public void serverExecute() {
	}

	public abstract String encode();

	public abstract void decode(Board board, String s);

	protected static String encode(Identifiable... args) {
		if(args == null){
			return null;
		}
		StringBuilder buf = new StringBuilder();
		for (Identifiable arg : args) {
			if (buf.length() != 0) {
				buf.append(":");
			}
			buf.append(arg.getId());
		}
		return buf.toString();
	}

	protected static String encodeObj(Object... args) {
		StringBuilder buf = new StringBuilder();
		for (Object arg : args) {
			if (buf.length() != 0) {
				buf.append(":");
			}
			buf.append(arg);
		}
		return buf.toString();
	}

	@Override
	public String toString() {
		return getClass().getName() + ": " + encode();
	}

	public void postServer(EarlDisplay basicDisplay) {
	}

}
