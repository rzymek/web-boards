package earl.client.games.bastogne;

import earl.client.ClientEngine;
import earl.client.data.Counter;
import earl.client.data.Hex;
import earl.client.display.DisplayHandler;
import earl.client.display.GameHandler;
import earl.client.games.SCSCounter;

public class BastogneHandler implements GameHandler {

	private DisplayHandler delegate;

	public BastogneHandler(DisplayHandler displayHandler) {
		this.delegate = displayHandler;
	}

	@Override
	public void areaClicked(Hex area) {
		delegate.areaClicked(area);
	}

	@Override
	public void pieceClicked(Counter piece) {
		if(piece instanceof SCSCounter) {
			SCSCounter c = (SCSCounter) piece;
			SCSCounter selected = (SCSCounter) delegate.getSelectedPiece();
			if(selected != null) {
				if(!c.getOwner().equals(selected.getOwner())) {
					ClientEngine.log(selected+" attacks "+c);
				}
			}
		}
		delegate.pieceClicked(piece);
	}

	@Override
	public void setSelectedPiece(Counter piece) {
		delegate.setSelectedPiece(piece);
	}

}
