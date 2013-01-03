package earl.client.display.svg.edit;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface EditServiceAsync {

	void save(Long id, String color, String src, AsyncCallback<Void> callback);

	void load(AsyncCallback<List<Map<String, String>>> callback);

}
