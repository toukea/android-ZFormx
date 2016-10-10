package android.istat.base.forms;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONObject;

import android.istat.base.forms.tools.FormTools;

/**
 * @author istat
 */
public class Form extends HashMap<String, Object> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public String[] getFieldNames() {
        return keySet().toArray(new String[keySet().size()]);
    }

    public String optString(String string) {
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
        return toJson().toString();
    }

    public void append(Form form) {
        FormTools.concatenate(this, form);
    }

    public void fillFrom(Object obj) {
        fillFormFromEntity(this, obj);
    }

    public <T> T as(Class<T> clazz) throws InstantiationException, IllegalAccessException {
        return createEntity(this, clazz);
    }

    public void flowOn(Object obj) {
        flowFormOnEntity(this, obj);
    }

    public final static Form createFrom(Object obj) {
        Form form = new Form();
        return fillFormFromEntity(form, obj);
    }

    public static final <T> T createEntity(Form form, Class<T> clazz) throws IllegalAccessException, InstantiationException {
        T instance = clazz.newInstance();
        flowFormOnEntity(form, clazz);
        return instance;
    }

    public static void flowFormOnEntity(Form form, Object obj) {
        flowFormOnEntity(form, obj, false);
    }

    public static void flowFormOnEntity(Form form, Object obj, boolean throwException) {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                String fieldName = field.getName();
                if (form.containsKey(fieldName)) {
                    field.setAccessible(true);
                    field.set(obj, form.get(fieldName));
                }
            } catch (Exception e) {
                if (throwException) {
//                    if (field.getType().equals(String.class)) {
//                        field.set(obj, form.get(fieldName));
//                    } else {
                    throw new RuntimeException(e);
//                    }
                }
            }
        }
    }
//    public static <T> T convertTo(Form form, Class<T> clazz) {
//        T obj = clazz.newInstance();
//        Field[] fields = obj.getClass().getDeclaredFields();
//        for (Field field : fields) {
//            try {
//                field.setAccessible(true);
//                form.put(field.getName(), field.get(obj));
//            } catch (Exception e) {
//
//            }
//        }
//        return null;
//    }

    public final static Form fillFormFromEntity(Form form, Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                form.put(field.getName(), field.get(obj));
            } catch (Exception e) {

            }
        }
        return form;
    }
}
