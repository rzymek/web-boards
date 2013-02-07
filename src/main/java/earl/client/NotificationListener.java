package earl.client;

import no.eirikb.gwtchannelapi.client.Channel;
import no.eirikb.gwtchannelapi.client.ChannelListener;
import no.eirikb.gwtchannelapi.client.Message;
import earl.client.data.Board;
import earl.client.data.GameCtx;
import earl.client.ops.Operation;

public final class NotificationListener implements ChannelListener {
	private final GameCtx ctx;

	public NotificationListener(GameCtx ctx) {
		this.ctx = ctx;
	}

	public void join(String channelToken) {
		Channel channel = new Channel(channelToken);
		channel.addChannelListener(this);
		channel.join();
	}

	@Override
	public void onReceive(Message message) {
		ClientEngine.log("Notif recv: "+message);
		OperationMessage msg = (OperationMessage) message;
		Operation op = msg.op;
		op.updateBoard(ctx.board);
		op.drawDetails(ctx.display);
		op.draw(ctx);
		op.postServer(ctx);
	}
	

}