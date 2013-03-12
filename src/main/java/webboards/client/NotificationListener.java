package webboards.client;

import webboards.client.data.GameCtx;

public final class NotificationListener {
	private final GameCtx ctx;

	public NotificationListener(GameCtx ctx) {
		this.ctx = ctx;
	}

	public void join(String channelToken) {
		ClientEngine.log("channel join:"+channelToken);
		if(channelToken != null) {
			try {
				join(channelToken, this);
			}catch(Exception ex){
				ClientEngine.log("failed to connect:"+ex.toString());
			}
		}
	}

	private native void join(String token, NotificationListener listener) /*-{
		channel = new $wnd.goog.appengine.Channel(token);
		socket = channel.open();
		socket.onmessage = function(data){
			listener.@webboards.client.NotificationListener::onMessage(Ljava/lang/String;)(data);
		}
		socket.onopen= listener.@webboards.client.NotificationListener::onOpen();
		socket.onerror = function(error) {
			listener.@webboards.client.NotificationListener::onError(Ljava/lang/String;)(error.description);
		}
	}-*/;

	public void onError(String desc) {
		ClientEngine.log("Channel API error:"+desc);		
	}
	public void onOpen() {
		ClientEngine.log("Connected");
	}
	
	public void onMessage(String data) {
		ClientEngine.log("Notif recv: "+data.toString());
//		OperationMessage msg = (OperationMessage) message;
//		Operation op = msg.op;
//		op.updateBoard(ctx.board);
//		op.postServer(ctx);
//		op.draw(ctx);
//		op.drawDetails(ctx);
	}
	

}