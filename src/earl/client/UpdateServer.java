package earl.client;

import earl.client.data.Counter;
import earl.client.data.Hex;
import earl.client.display.GameChangeListener;
import earl.client.remote.ServerEngineAsync;
import earl.client.utils.AbstractCallback;

public class UpdateServer implements GameChangeListener {

	private ServerEngineAsync service;

	public UpdateServer(ServerEngineAsync service) {
		this.service = service;
	}

	@Override
	public void counterMoved(Counter counter, Hex from, Hex to) {
		service.counterMoved(counter, from, to, new AbstractCallback<Void>() {
		});
	}

	@Override
	public void counterChanged(Counter piece) {
		service.counterChanged(piece, new AbstractCallback<Void>() {
		});
	}

}
