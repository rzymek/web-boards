package earl.client.op;

import java.io.Serializable;

import earl.client.bastogne.op.OperationContext;
import earl.client.data.Board;
import earl.client.data.Identifiable;

public abstract class Operation implements Serializable {
	public abstract void draw(EarlDisplay g);

	public abstract void execute(OperationContext ctx);

	public void drawDetails(EarlDisplay g) {
	}

	public boolean isValid(OperationContext ctx) {
		return true;
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

	@Override
	public String toString() {
		return getClass().getName() + ": " + encode();
	}

}
