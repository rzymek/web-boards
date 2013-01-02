package earl.server;

import earl.client.ex.EarlServerException;

public class Op {
	private Type type;
	private String[] args;

	public static enum Type {
		ROLL, CONNECTED, CHAT
	};

	public Op(Type type, String... args) {
		this.type = type;
		this.args = args;
	}

	public Op(Type type, int... args) {
		this(type, toString(args));
	}

	private static String[] toString(int[] args) {
		String[] s = new String[args.length];
		for (int i = 0; i < args.length; i++) {
			s[i] = String.valueOf(args[i]);
		}
		return s;
	}

	public String toMessage() {
		if (args.length == 0) {
			return type.name();
		}
		StringBuilder b = new StringBuilder();
		b.append(type.name());
		for (String arg : args) {
			if (arg.contains(":")) {
				throw new EarlServerException("Op arguments can't contain ':'. Op.Type=" + type + ", arg=" + arg);
			}
			b.append(':').append(arg);
		}
		return b.toString();
	}

	public Type getType() {
		return type;
	}

	public String[] getArgs() {
		return args;
	}

	@Override
	public String toString() {
		switch (type) {
		case CONNECTED:
			return "Client connected: " + args[0];
		case ROLL:
			String s = args[0] + "d" + args[1] + " = " + args[2] + " s(";
			for (int i = 3; i < args.length; i++) {
				s += args[i];
				if (i < args.length - 1) {
					s += ", ";
				}
			}
			s += ")";
			return s;
		case CHAT:
			return ">>> "+args[0];
		}
		throw new EarlServerException("Unsupported type:" + type);
	}
}
