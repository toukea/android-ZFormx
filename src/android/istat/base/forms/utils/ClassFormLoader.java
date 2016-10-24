package android.istat.base.forms.utils;

import java.lang.reflect.Field;
import java.util.HashMap;

import android.istat.base.forms.Form;

public abstract class ClassFormLoader<T> {
	final static HashMap<Class<?>, ClassFormLoader<?>> loader = new HashMap<Class<?>, ClassFormLoader<?>>();

	public final static ClassFormLoader<Object> getDefaultLoader() {
		return DEFAULT_LOADER;
	}

	@SuppressWarnings("unchecked")
	public static final <T> ClassFormLoader<T> getLoader(Class<T> clazz) {
		if (!loader.containsKey(clazz)) {
			return null;
		}
		return (ClassFormLoader<T>) loader.get(clazz);
	}

	@SuppressWarnings("unchecked")
	public final static <T> void flowFormOn(Form form, T obj) {
		if (!loader.containsKey(obj.getClass())) {
			DEFAULT_LOADER.onFlowFormOn(form, obj);
			return;
		}

		ClassFormLoader<T> formLoader = (ClassFormLoader<T>) loader.get(obj
				.getClass());
		formLoader.onFlowFormOn(form, obj);
	}

	@SuppressWarnings("unchecked")
	public final static <T> void fillFormFrom(Form form, T obj) {
		if (!loader.containsKey(obj.getClass())) {
			DEFAULT_LOADER.onFillFormFrom(form, obj);
			return;
		}

		ClassFormLoader<T> formLoader = (ClassFormLoader<T>) loader.get(obj
				.getClass());
		formLoader.onFillFormFrom(form, obj);
	}

	public abstract void onFlowFormOn(Form form, T entity);

	public abstract void onFillFormFrom(Form form, T entity);

	final static ClassFormLoader<Object> DEFAULT_LOADER = new ClassFormLoader<Object>() {

		@Override
		public void onFlowFormOn(Form form, Object obj) {
			Class<?> clazz = obj.getClass();
			Field[] fields = clazz.getDeclaredFields();
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

		@Override
		public void onFillFormFrom(Form form, Object entity) {
			// TODO Auto-generated method stub
			Field[] fields = entity.getClass().getDeclaredFields();
			for (Field field : fields) {
				try {
					field.setAccessible(true);
					form.put(field.getName(), field.get(entity));
				} catch (Exception e) {

				}
			}
		}
	};
}
