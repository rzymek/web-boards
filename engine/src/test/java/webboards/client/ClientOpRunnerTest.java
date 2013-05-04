package webboards.client;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import webboards.client.data.GameCtx;
import webboards.client.display.Display;
import webboards.client.ops.Operation;
import webboards.client.ops.generic.DiceRoll;
import webboards.client.remote.ServerEngineAsync;

import com.google.gwt.dom.client.Style.Visibility;

@RunWith(MockitoJUnitRunner.class)
public class ClientOpRunnerTest {	
	@Mock
	GameCtx ctx;
	@Mock
	Display display;
	
	ClientOpRunner runner;
	
	ServerEngineMock service = spy(new ServerEngineMock());

	@Before
	public void setUp() throws Exception {
		ctx.display = display;
		doReturn(false).when(ctx).isHistoryMode();
		runner = Mockito.spy(new ClientOpRunner(ctx){
			@Override
			protected boolean ask(String msg) {
				return false;
			}
			@Override
			protected ServerEngineAsync getServerService() {
				return service;
			}
			@Override
			protected void setAjaxVisibility(Visibility visibility) {
			}
			@Override
			protected void reload() {
			}
		});
		ctx.ops = new ArrayList<Operation>();
		Mockito.reset(service);
		service.doThrow=true;
	}

	@Test
	public void processRefresh() {
		doReturn(false).when(runner).ask(anyString());
		runner.process(new DiceRoll());
		verify(runner, times(1)).reload();
//		verify(service).getState(eq(1l), (AsyncCallback<GameInfo>) anyObject());
	}

	@Test
	public void processCancel() {
		doReturn(true).when(runner).ask(anyString());
		DiceRoll op = new DiceRoll();
		runner.process(op);
//		verify(service, times(2)).process(op, runner);
		verify(runner, never()).reload();
	}
}
