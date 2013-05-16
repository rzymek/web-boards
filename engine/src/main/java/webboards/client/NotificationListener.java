package webboards.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Window;

import no.eirikb.gwtchannelapi.client.Channel;
import no.eirikb.gwtchannelapi.client.ChannelListener;
import no.eirikb.gwtchannelapi.client.Message;
import webboards.client.data.GameCtx;
import webboards.client.remote.ServerEngine;
import webboards.client.remote.ServerEngineAsync;
import webboards.client.utils.AbstractCallback;

public final class NotificationListener implements ChannelListener {
	private ClientOpRunner runner;

	public NotificationListener(GameCtx ctx) {
		runner = new ClientOpRunner(ctx);
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
	
	@Override
	public void onOpen() {
		ClientEngine.log("Connected");
	}
	
	@Override
	public void onReceive(Message message) {
		OperationMessage msg = (OperationMessage) message;
		runner.apply(msg.op);
	}

	@Override
	public void onError(int code, String description) {
		String msg = "Instant notifications error: "+code+": "+description+"\n" +
				"Would you like to reestablish instant notifications?";
		if(!Window.confirm(msg)) {
			return;
		}
		ServerEngineAsync service = GWT.create(ServerEngine.class);
		service.reopenInstantNotif(new AbstractCallback<String>(){
			@Override
			public void onSuccess(String channelToken) {
				join(channelToken);
			}
		});
	}

	@Override
	public void onClose() {
		ClientEngine.log("Dis-connected");		
	}
}