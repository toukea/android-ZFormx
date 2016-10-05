package android.istat.base.forms;

import java.util.HashMap;
import java.util.Iterator;
import org.json.JSONObject;

public class Form extends HashMap<String, Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String[] getFieldNames() {
		// TODO Auto-generated method stub

		return keySet().toArray(new String[keySet().size()]);
	}

	public String optString(String string) {
		// TODO Auto-generated method stub
		Object value = get(string);
		return value != null ? value.toString() : "";
	}

	public int optInt(String name) {
		try {
			return Integer.valueOf(name);
		} catch (Exception e) {
			return 0;
		}
	}

	public int optLong(String name) {
		try {
			return Integer.valueOf(name);
		} catch (Exception e) {
			return 0;
		}
	}

	public float optFloat(String name) {
		try {
			return Float.valueOf(name);
		} catch (Exception e) {
			return 0;
		}
	}

	public double optDouble(String name) {
		try {
			return Double.valueOf(name);
		} catch (Exception e) {
			return 0;
		}
	}

	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		try {
			Iterator<String> iterator = keySet().iterator();
			while (iterator.hasNext()) {
				String name = iterator.next();
				optString(name);
				json.put(name, optString(name));
			}
		} catch (Exception e) {

		}
		return json;
	}

	public final static Form fromJson(String json) {
		Form form = new Form();
		try {
			JSONObject jsonO = new JSONObject(json);
			@SuppressWarnings("rawtypes")
			Iterator keys = jsonO.keys();
			while (keys.hasNext()) {
				String name = keys.next().toString();
				form.put(name, jsonO.opt(name));
			}
		} catch (Exception e) {

		}
		return form;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return toJson().toString();
	}
}
