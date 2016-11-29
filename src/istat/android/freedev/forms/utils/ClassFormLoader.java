package istat.android.freedev.forms.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import istat.android.freedev.forms.Form;
import istat.android.freedev.forms.tools.FormTools;

import android.os.Bundle;

public abstract class ClassFormLoader<T> {
    private final static HashMap<Class<?>, ClassFormLoader<?>> objectLoader = new HashMap<Class<?>, ClassFormLoader<?>>() {
        {
            put(Bundle.class, DEFAULT_BUNDLE_OBJECT_LOADER);
        }
    };
    private final static HashMap<Class<?>, ClassFormLoader<?>> formLoader = new HashMap<Class<?>, ClassFormLoader<?>>() {
        {
            put(Bundle.class, DEFAULT_BUNDLE_FORM_LOADER);
        }
    };


    @SuppressWarnings("unchecked")
    public final static <T> void putAsObjectLoader(ClassFormLoader<T> newLoader) {
        Class<T> clazz = (Class<T>) FormTools.getGenericTypeClass(
                newLoader.getClass(), 0);
        objectLoader.put(clazz, newLoader);
    }

    @SuppressWarnings("unchecked")
    public final static <T> void putAsFormLoader(ClassFormLoader<T> newLoader) {
        Class<T> clazz = (Class<T>) FormTools.getGenericTypeClass(
                newLoader.getClass(), 0);
        formLoader.put(clazz, newLoader);
    }

    @SuppressWarnings("unchecked")
    public final static <T> ClassFormLoader<T> getLoader(Class<T> clazz) {
        if (!formLoader.containsKey(clazz)) {
            return null;
        }
        return (ClassFormLoader<T>) formLoader.get(clazz);
    }

    @SuppressWarnings("unchecked")
    public final static <T> void flowFormOn(Form form, T obj) {
        if (!formLoader.containsKey(obj.getClass())) {
            DEFAULT_OBJECT_LOADER.load(form, obj);
            return;
        }

        ClassFormLoader<T> loader = (ClassFormLoader<T>) ClassFormLoader.objectLoader
                .get(obj.getClass());
        loader.onLoad(form, obj);
    }

    @SuppressWarnings("unchecked")
    public final static <T> void fillFormFrom(Form form, T obj) {
        if (!formLoader.containsKey(obj.getClass())) {
            DEFAULT_FORM_LOADER.load(form, obj);
            return;
        }
        ClassFormLoader<T> loader = (ClassFormLoader<T>) ClassFormLoader.formLoader
                .get(obj.getClass());
        loader.load(form, obj);
    }

    public final void load(Form form, T entity) {
        onLoad(form, entity);
    }

    protected abstract void onLoad(Form form, T entity);

    final static ClassFormLoader<Object> DEFAULT_FORM_LOADER = new ClassFormLoader<Object>() {

        @Override
        public void onLoad(Form form, Object entity) {
            List<Field> fields = FormTools.getAllFieldFields(entity.getClass(), true, false);
            for (Field field : fields) {
                try {
                    field.setAccessible(true);
                    form.put(field.getName(), field.get(entity));
                } catch (Exception e) {

                }
            }
        }
    };
    final static ClassFormLoader<Object> DEFAULT_OBJECT_LOADER = new ClassFormLoader<Object>() {

        @Override
        public void onLoad(Form form, Object obj) {
            Class<?> clazz = obj.getClass();
            List<Field> fields = FormTools.getAllFieldFields(clazz, true, false);
            for (Field field : fields) {
                try {
                    String fieldName = field.getName();
                    if (form.containsKey(fieldName)) {
                        field.setAccessible(true);
                        Object fieldValue = form.get(fieldName);
                        field.set(obj, fieldValue);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    };

    final static ClassFormLoader<Bundle> DEFAULT_BUNDLE_FORM_LOADER = new ClassFormLoader<Bundle>() {

        @Override
        public void onLoad(Form form, Bundle bundle) {
            Iterator<String> iterator = bundle.keySet().iterator();
            while (iterator.hasNext()) {
                String value = iterator.next();
                form.put(value, bundle.get(value));
            }
        }
    };

    final static ClassFormLoader<Bundle> DEFAULT_BUNDLE_OBJECT_LOADER = new ClassFormLoader<Bundle>() {

        @Override
        public void onLoad(Form form, Bundle bundle) {
            for (String tmp : form.getFieldNames()) {
                bundle.putString(tmp, form.optString(tmp));
            }
        }
    };

    public static ClassFormLoader<Object> getDefaultFormLoader() {
        return DEFAULT_FORM_LOADER;
    }

    public final static ClassFormLoader<Object> getDefaultObjectLoader() {
        return DEFAULT_OBJECT_LOADER;
    }

}
