package webboards.client.games.scs;

import java.io.Serializable;

import webboards.client.ex.EarlException;

public class CombatResult implements Serializable {
	private static final long serialVersionUID = 1L;
	private transient static final String DIGITS = "0123456789";
	public int A;
	public int D;
	public int r;

	protected CombatResult() {
	}

	public CombatResult(String value) {
		char cmd = '\0';
		for (int i = 0; i < value.length(); i++) {
			char c = value.charAt(i);
			if(c == ' ' || c == '\t') {
				continue;
			}
			if (DIGITS.indexOf(c) != -1) {
				int numVal = Integer.parseInt(String.valueOf(c));
				if ('A' == cmd) {
					A = numVal;
				} else if ('D' == cmd) {
					D = numVal;
				} else if ('r' == cmd) {
					r = numVal;
				} else {
					throw new EarlException("Invalid combat result value:" + value + " @" + i);
				}
			}else{
				cmd = c;
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		if (A != 0) {
			buf.append("A").append(A);
		}
		if (D != 0) {
			buf.append("D").append(D);
		}
		if (r != 0) {
			buf.append("r").append(r);
		}
		return buf.toString();
	}
}
