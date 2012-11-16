package earl.server;

import earl.server.notify.Notify;

@Deprecated
public class DisabledNotify extends Notify {

	@Override
	public String openChannel(String tableId, String user) {
		return "disabled";
	}
	
	@Override
	public void notifyListeners(String tableId, Op op) {
	}
	
}
