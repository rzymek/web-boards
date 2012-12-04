package earl.client.bastogne.op;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.googlecode.objectify.annotation.EntitySubclass;

@EntitySubclass
public class OperationContext {
	private final Map<Object, Collection<Object>> ctx = new HashMap<Object, Collection<Object>>();

	public void add(Object key, Object value) {
		Collection<Object> values = ctx.get(key);
		if (values == null) {
			values = new ArrayList<Object>();
			ctx.put(key, values);
		}
		values.add(value);
	}

	public void clear(Object key) {
		ctx.remove(key);
	}

	public Collection<Object> get(Object key) {
		Collection<Object> values = ctx.get(key);
		return values == null ? Collections.emptyList() : values;
	}
}
