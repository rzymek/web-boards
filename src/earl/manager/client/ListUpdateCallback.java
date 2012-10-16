package earl.manager.client;

import java.util.List;

import com.google.gwt.user.client.ui.ListBox;

public class ListUpdateCallback extends ManagerCallback<List<String>> {

	private final ListBox list;

	public ListUpdateCallback(ListBox list) {
		this.list = list;
	}

	@Override
	public void onSuccess(List<String> result) {
		list.clear();
		for (String string : result) {
			list.addItem(string);
		}
	}

}
