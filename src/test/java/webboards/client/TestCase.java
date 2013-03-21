package webboards.client;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import webboards.client.data.GameCtx;
import webboards.client.display.EarlDisplay;
import webboards.client.ops.Operation;
import webboards.client.remote.ServerEngine;
import webboards.client.remote.ServerEngineAsync;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.core.shared.GWTBridge;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.impl.WindowImpl;

@RunWith(MockitoJUnitRunner.class)
public class TestCase {
	@InjectMocks
	GameCtx ctx;
	@Mock
	EarlDisplay display;
	ClientOpRunner runner;
	ServerEngineAsync service;
	@Mock
	WindowImpl windowImpl;

	@Before
	public void setUp() throws Exception {
		GWTBridge bridge = mock(GWTBridge.class);
		GWT.setBridge(bridge);
		service = spy(new ServerEngineMock());
		doReturn(service).when(bridge).create(ServerEngine.class);
		doReturn(windowImpl).when(bridge).create(WindowImpl.class);
		Field field = Window.Location.class.getDeclaredField("paramMap");
		field.setAccessible(true);
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("table", "1");
		field.set(null, paramMap);
		runner = Mockito.spy(new ClientOpRunner(ctx));
		ctx.ops = new ArrayList<Operation>();
	}
}
