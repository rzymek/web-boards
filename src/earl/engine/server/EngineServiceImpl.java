package earl.engine.server;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import earl.engine.client.EngineService;

public class EngineServiceImpl extends RemoteServiceServlet implements
		EngineService {
	private static final long serialVersionUID = 1L;

	@Override
	public void updateLocation(String unitId, String hexId) {
		Map<String, String> units = getUnits();
		units.put(unitId, hexId);
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> getUnits() {
		ServletContext ctx = getServletContext();
		Map<String,String> units = (Map<String, String>) ctx.getAttribute("units");
		if(units == null) {
			ctx.setAttribute("units", units = new HashMap<String, String>());
		}
		return units;
	}	
}
