package earl.client.op;

import java.io.Serializable;

import earl.client.bastogne.op.OperationContext;
import earl.client.data.Board;
import earl.client.data.Identifiable;

public abstract class Operation implements Serializable {
	public void draw(EarlDisplay g) {		
	}

	public void execute(OperationContext ctx){
		
	}

	public void drawDetails(EarlDisplay g) {
	}

	public void serverExecute() {

	}

	public abstract String encode();

	public abstract void decode(Board board, String s);

	protected static String encode(Identifiable... args) {
		StringBuilder buf = new StringBuilder();
		for (Identifiable arg : args) {
			if (buf.length() != 0) {
				buf.append(":");
			}
			buf.append(arg.getId());
		}
		return buf.toString();
	}

	protected static String encode(Object... args) {
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

}
