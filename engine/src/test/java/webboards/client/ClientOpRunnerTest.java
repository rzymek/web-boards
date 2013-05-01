package webboards.client;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import webboards.client.data.GameCtx;
import webboards.client.data.GameInfo;
import webboards.client.display.EarlDisplay;
import webboards.client.ops.Operation;
import webboards.client.ops.generic.DiceRoll;
import webboards.client.remote.ServerEngine;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.core.shared.GWTBridge;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.impl.WindowImpl;
import com.google.gwt.user.client.rpc.AsyncCallback;

@RunWith(MockitoJUnitRunner.class)
public class ClientOpRunnerTest {
	@InjectMocks
	GameCtx ctx;
	@Mock
	EarlDisplay display;
	ClientOpRunner runner;
	static ServerEngineMock service = spy(new ServerEngineMock());

	@Before
	public void setUp() throws Exception {
		GWTBridge bridge = mock(GWTBridge.class);
		GWT.setBridge(bridge);
		Mockito.reset(service);
		doReturn(service).when(bridge).create(ServerEngine.class);
		doReturn(mock(WindowImpl.class)).when(bridge).create(WindowImpl.class);
		Field field = Window.Location.class.getDeclaredField("paramMap");
		field.setAccessible(true);
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("table", "1");
		field.set(null, paramMap);
		runner = Mockito.spy(new ClientOpRunner(ctx));
		ctx.ops = new ArrayList<Operation>();
		service.doThrow=true;
	}

	@SuppressWarnings("unchecked")
	@Test @Ignore
	public void processRefresh() {
		doReturn(false).when(runner).ask(anyString());
		runner.process(new DiceRoll());
		System.out.println("processRefresh.service="+service);
		verify(service).getState(eq(1l), (AsyncCallback<GameInfo>) anyObject());
	}

	@SuppressWarnings("unchecked")
	@Test @Ignore
	public void processCancel() {
		doReturn(true).when(runner).ask(anyString());
		DiceRoll op = new DiceRoll();
		runner.process(op);
		System.out.println("processCancel.service="+service);
		verify(service, times(2)).process(op, runner);
		verify(service, never()).getState(eq(1l), (AsyncCallback<GameInfo>) any());
	}
}
