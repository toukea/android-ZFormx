package istat.android.freedev.forms;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;

import istat.android.freedev.forms.tools.FormTools;
import istat.android.freedev.forms.utils.ClassFormLoader;

/**
 * @author istat
 */
public class Form extends HashMap<String, Object> {
    public final static Object DEFAULT_EMPTY_VALUE = "";

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    Object emptyValue = DEFAULT_EMPTY_VALUE;

    public String[] getFieldNames() {
        return keySet().toArray(new String[keySet().size()]);
    }

    public Class<?> getFieldTypeClass(String field) {
        if (containsKey(field)) {
            Object obj = get(field);
            if (obj != null) {
                return obj.getClass();
            }
        }
        return Object.class;
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

    public void flowInto(Object obj) {
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

    public <T> void flowInto(T obj, ClassFormLoader<T> loader) {
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

    public boolean clearValue(String... values) {
        if (values == null || values.length == 0) {
            return this.values().removeAll(Collections.singletonList(null));
        }
        List<String> valueArray = Arrays.asList(values);
        return this.values().removeAll(valueArray);
    }

    public int clearkey(String... values) {
        if (values == null) {
            return 0;
        }
        List<String> arrayList = Arrays.asList(values);
        Iterator<String> iterator = keySet().iterator();
        int count = 0;
        while (iterator.hasNext()) {
            String name = iterator.next();
            if (arrayList.contains(name)) {
                remove(name);
                count++;
            }
        }
        return count;
    }

    /**
     * define which value will be the default empty value for auto created field.
     *
     * @param emptyValue
     */
    public void setEmptyValue(Object emptyValue) {
        this.emptyValue = emptyValue;
    }

    /**
     * Add some value to the form. with default value.
     * defined by {@link #setEmptyValue(Object)}.
     * default empty value is {@link #DEFAULT_EMPTY_VALUE}
     *
     * @param fields
     */
    public void setFieldNames(String... fields) {
        List<String> tmp = new ArrayList<String>();
        Collections.addAll(tmp, fields);
        String[] currentField = getFieldNames();
        for (String field : currentField) {
            if (!tmp.contains(field)) {
                remove(field);
            }
        }
        for (String field : tmp) {
            if (!this.containsKey(field)) {
                addFieldName(field);
            }
        }
    }

    /**
     * Add some value to the form. with default value.
     * defined by {@link #setEmptyValue(Object)}.
     * default empty value is {@link #DEFAULT_EMPTY_VALUE}
     *
     * @param fields
     */
    public void addFieldNames(String... fields) {
        if (fields != null && fields.length > 0) {
            for (String field : fields) {
                put(field, emptyValue);
            }
        }
    }

    /**
     * Add field to form with an empty empty value. defined by {@link #setEmptyValue(Object)}.
     * default empty value is {@link #DEFAULT_EMPTY_VALUE}
     *
     * @param field
     */
    public void addFieldName(String field) {
        addFieldNames(field);
    }

    public static <T> Form createClass(Class<T> managedClass) {
        return createClass(managedClass, null);
    }

    public static <T> Form createClass(Class<T> managedClass, Object emptyValue) {
        List<Field> fields = FormTools.getAllFieldFields(managedClass, true, false);
        String[] fieldNames = new String[fields.size()];
        for (int i = 0; i < fields.size(); i++) {
            fieldNames[i] = fields.get(i).getName();
        }
        Form form = new Form();
        form.setEmptyValue(emptyValue);
        form.setFieldNames(fieldNames);
        return form;
    }
}