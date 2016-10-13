package android.istat.base.forms;

import android.istat.base.forms.tools.FormTools;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

/**
 * @author istat
 */
abstract class FormGetSetter {
	protected Form form;
	protected boolean modifyEditableOnly = false;

	FormGetSetter(Form form) {
		this.form = form;
	}

	protected final void mutateView(View formBaseView) {
		if (formBaseView != null) {
			if (formBaseView instanceof ViewGroup) {
				traitViewAsViewGroup((ViewGroup) formBaseView);
			} else {
				traitViewAsSimpleView(formBaseView);
			}
		}
	}

	protected abstract void traitViewAsViewGroup(ViewGroup v);

	protected final void traitViewAsSimpleView(View v) {
		if (modifyEditableOnly && !v.isEnabled()) {
			return;
		}
		if (v.getTag() != null && !FormTools.isEmpty(v.getTag())) {
			performMutation(v);
		}
	}

	private void performMutation(View v) {
		if (fieldHandlers != null && fieldHandlers.size() > 0) {
			for (FieldValueGetSetter handler : fieldHandlers) {
				boolean result = handler.onHandle(form, v.getTag() + "", v);
				if (result) {
					return;
				}
			}
		}
		throw new RuntimeException("unsuported view for form autoBind::"
				+ v.getClass());
	}

	protected final List<FieldValueGetSetter> fieldHandlers = new ArrayList<FieldValueGetSetter>();

	protected void addAllFieldHandlers(List<FieldValueGetSetter> handlers) {
		if (handlers != null && handlers.size() > 0) {
			fieldHandlers.addAll(handlers);
		}
		fieldHandlers.add(getDefaultHandler());
	}

	protected final void setModifyEditableOnly(boolean modifyEditableOnly) {
		this.modifyEditableOnly = modifyEditableOnly;
	}

	protected final boolean isModifyEditableOnly() {
		return modifyEditableOnly;
	}

	protected abstract FieldValueGetSetter getDefaultHandler();

	static abstract class FieldValueGetSetter {
		boolean override = false;

		public FieldValueGetSetter() {

		}

		public FieldValueGetSetter(boolean override) {
			this.override = override;
		}

		protected final boolean isHandlable(View view) {
			Class<?> clazzView = getViewTypeClass();
			return (view.getClass().isAssignableFrom(clazzView)
					|| clazzView.isAssignableFrom(view.getClass()) || clazzView
					.equals(view.getClass()));
		}

		protected final Class<?> getFieldValueTypeClass() {
			try {
				String className = ((ParameterizedType) getClass()
						.getGenericSuperclass()).getActualTypeArguments()[0]
						.toString().replaceFirst("class", "").trim();
				Class<?> clazz = Class.forName(className);
				// Log.d("FieldValueGetSetter",
				// "getFieldValueTypeClass::clazz[0]=" + clazz);
				return clazz;
			} catch (Exception e) {
				throw new IllegalStateException(
						"Class is not parametrized with generic type!!! Please use extends <> ");
			}
		}

		@SuppressWarnings("unchecked")
		protected final <T> Class<T> getViewTypeClass() {
			try {
				String className = ((ParameterizedType) getClass()
						.getGenericSuperclass()).getActualTypeArguments()[1]
						.toString().replaceFirst("class", "").trim();
				Class<?> clazz = Class.forName(className);
				// Log.d("FieldValueGetSetter", "getViewTypeClass::clazz[1]="
				// + clazz);
				return (Class<T>) clazz;
			} catch (Exception e) {
				throw new IllegalStateException(
						"Class is not parametrized with generic type!!! Please use extends <> ");
			}
		}

		public boolean onHandle(Form form, String fieldName, View view) {
			boolean assignableLeft = true;
			boolean assignableRight = true;
			boolean equals = true;
			if (view != null) {
				assignableLeft = view.getClass().isAssignableFrom(
						getViewTypeClass());
				assignableRight = getViewTypeClass().isAssignableFrom(
						view.getClass());
				equals = getViewTypeClass().equals(view.getClass());

			}
			return this.override || assignableLeft || assignableRight || equals;
		}
	}
}
