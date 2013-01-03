package earl.client.display.svg.edit;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("edit")
public interface EditService extends RemoteService {

	void save(Long id, String color, String src) throws Exception;

	List<Map<String, String>> load();

}
