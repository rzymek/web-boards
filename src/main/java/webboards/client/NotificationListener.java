package webboards.client;

import no.eirikb.gwtchannelapi.client.Channel;
import no.eirikb.gwtchannelapi.client.ChannelListener;
import no.eirikb.gwtchannelapi.client.Message;
import webboards.client.data.GameCtx;
import webboards.client.ops.Operation;
import webboards.client.utils.Utils;

public final class NotificationListener implements ChannelListener {
	private final GameCtx ctx;

	public NotificationListener(GameCtx ctx) {
		this.ctx = ctx;
	}

	public void join(String channelToken) {
		ClientEngine.log("channel join:"+channelToken);
		if(channelToken != null) {
			try {
				Channel channel = new Channel(channelToken);
				channel.addChannelListener(this);
				channel.join();
			}catch(Exception ex){
				ClientEngine.log("failed to connect:"+ex.toString());
			}
		}
	}

	public void onError(String desc) {
		ClientEngine.log("Channel API error:"+desc);		
	}
	public void onOpen() {
		ClientEngine.log("Connected");
	}
	
	@Override
	public void onReceive(Message message) {
		OperationMessage msg = (OperationMessage) message;
		Operation op = msg.op;
		Utils.set(ctx.ops, op.index, op);
		op.updateBoard(ctx.board);
		op.postServer(ctx);
		op.draw(ctx);
		op.drawDetails(ctx);		
	}
	

}