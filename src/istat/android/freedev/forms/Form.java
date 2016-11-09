package istat.android.freedev.forms;

import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONObject;

import istat.android.freedev.forms.tools.FormTools;
import istat.android.freedev.forms.utils.ClassFormLoader;

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

    public <T> T opt(String name) {
        try {
            return (T) super.get(name);
        } catch (Exception e) {
            return null;
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

    public <T> T as(Class<T> clazz) throws InstantiationException,
            IllegalAccessException {
        return createEntity(this, clazz);
    }

    public void flowOn(Object obj) {
        flowFormOnEntity(this, obj);
    }

    //----------------------------------
    public <T> void fillFrom(T obj, ClassFormLoader<T> loader) {
        fillFormFromEntity(this, obj, loader);
    }

    public <T> T as(Class<T> clazz, ClassFormLoader<T> loader) throws InstantiationException,
            IllegalAccessException {
        return createEntity(this, clazz, loader);
    }

    public <T> void flowOn(T obj, ClassFormLoader<T> loader) {
        flowFormOnEntity(this, obj, loader);
    }

    //------------------------------------------------------
    public final static <T> Form createFrom(T obj, ClassFormLoader<T> loader) {
        Form form = new Form();
        return fillFormFromEntity(form, obj, loader);
    }

    public final static <T> Form createFrom(T obj) {
        return createFrom(obj, null);
    }

    public static final <T> T createEntity(Form form, Class<T> clazz) throws InstantiationException, IllegalAccessException {
        return createEntity(form, clazz, null);
    }

    public static final <T> T createEntity(Form form, Class<T> clazz, ClassFormLoader<T> loader)
            throws IllegalAccessException, InstantiationException {
        T instance = clazz.newInstance();
        flowFormOnEntity(form, instance, loader);
        return instance;
    }

    public static <T> void flowFormOnEntity(Form form, T obj) {
        flowFormOnEntity(form, obj, null);
    }


    public final static <T> Form fillFormFromEntity(Form form, T obj) {
        return fillFormFromEntity(form, obj, null);
    }

    public final static <T> Form fillFormFromEntity(Form form, T obj, ClassFormLoader<T> loader) {
        if (loader != null) {
            loader.load(form, obj);
        } else {
            ClassFormLoader.fillFormFrom(form, obj);
        }
        return form;
    }

    public static <T> void flowFormOnEntity(Form form, T obj, ClassFormLoader<T> loader) {
        if (loader != null) {
            loader.load(form, obj);
        } else {
            ClassFormLoader.flowFormOn(form, obj);
        }

    }
}
