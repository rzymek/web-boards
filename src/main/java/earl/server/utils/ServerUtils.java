package earl.server.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;

import earl.client.ex.EarlServerException;

public final class ServerUtils {
	private ServerUtils() {}
	public static byte[] serialize(Serializable op) {
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		try {
			ObjectOutputStream out = new ObjectOutputStream(buf);
			try {
				out.writeObject(op);
			}finally{
				out.close();
			}
		}catch (IOException e) {
			throw new EarlServerException(e);
		}
		return buf.toByteArray();
	}
	public static String describe(Object obj) {
		if(obj == null) {
			return "nil";
		}
		Class<?> type = obj.getClass();		
		Field[] fields = type.getFields();
		StringBuilder buf = new StringBuilder();
//		buf.append(type.getSimpleName()).append("\n");
		for (Field field : fields) {
			buf.append(field.getName()).append(":");
			try {
				field.setAccessible(true);
				Object value = field.get(obj);
				buf.append(value);
			}catch(Exception ex){
				buf.append(ex.toString());
			}
			buf.append("\n");
		}
		return buf.toString();
	}

}
