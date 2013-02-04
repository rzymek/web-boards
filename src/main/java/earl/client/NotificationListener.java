package earl.client;

import no.eirikb.gwtchannelapi.client.Channel;
import no.eirikb.gwtchannelapi.client.ChannelListener;
import no.eirikb.gwtchannelapi.client.Message;
import earl.client.data.Board;
import earl.client.menu.EarlClienContext;
import earl.client.ops.Operation;

public final class NotificationListener implements ChannelListener {
	private final EarlClienContext ctx;

	public NotificationListener(EarlClienContext ctx) {
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
		Board board = ctx.game.getBoard();
		Operation op = msg.op;
		op.updateBoard(board);
		op.drawDetails(ctx.display);
		op.draw(board, ctx.display);
		op.postServer(board, ctx.display);
	}
	

}