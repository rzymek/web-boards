package earl.client.games.scs;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.rpc.IsSerializable;

import earl.client.data.Hex;
import earl.client.games.scs.bastogne.HexTraits;
import earl.client.ops.Operation;

public class SCSHex extends Hex implements Serializable, IsSerializable {
	private static final long serialVersionUID = 1L;
	private Set<HexTraits> traits = null;

	public SCSHex() {
		traits = Collections.emptySet();
	}

	public SCSHex(HexTraits... traits) {
		this.traits = new HashSet<HexTraits>(Arrays.asList(traits));
	}

	public Set<HexTraits> getTraits() {
		return traits;
	}

	public boolean isAdjacent(SCSHex hex) {
		// TODO Auto-generated method stub
		return false;
	}

	public List<SCSHex> getAdjacent() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Operation onClicked() {
		return null;
	}
}
