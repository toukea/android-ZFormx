package android.istat.base.forms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.istat.base.forms.tools.FormTools;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * @author istat
 */
public final class FormFlower extends FormGetSetter {

	FormFlower(Form form) {
		super(form);
	}

	public static FormFlower flowIntoView(Form form, View view,
			FieldValueSetter<?, ?>... fieldHandlers) {
		return flowIntoView(form, view, false, fieldHandlers);
	}

	public static FormFlower flowIntoView(Form form, View view,
			boolean editableOnly, FieldValueSetter<?, ?>... fieldHandlers) {
		return flowIntoView(form, view, editableOnly,
				fieldHandlers != null ? Arrays.asList(fieldHandlers) : null);
	}

	public static FormFlower flowIntoView(Form form, View view,
			boolean editableOnly, List<FieldValueSetter<?, ?>> fieldHandlers) {
		FormFlower flower = new FormFlower(form);
		List<FieldValueGetSetter> handlers = new ArrayList<FieldValueGetSetter>();
		if (fieldHandlers != null && fieldHandlers.size() > 0) {
			handlers.addAll(fieldHandlers);
		}
		flower.addAllFieldHandlers(handlers);
		flower.setModifyEditableOnly(editableOnly);
		flower.mutateView(view);
		return flower;
	}

	public static abstract class FieldValueSetter<T, V extends View> extends
			FieldValueGetSetter {

		public abstract boolean setValue(T entity, V v);

		@SuppressWarnings("unchecked")
		@Override
		public final boolean onHandle(Form form, String fieldName, View view) {
			if (isHandlable(view)) {
				T value = form.opt(fieldName);
				return setValue(value, (V) view);
			}
			return super.onHandle(form, fieldName, view);
		}
	}

	public final static FieldValueSetter<Integer, Spinner> SETTER_SPINNER_INDEX = new FieldValueSetter<Integer, Spinner>() {

		@Override
		public boolean setValue(Integer entity, Spinner spinner) {

			return false;
		}
	};
	public final static FieldValueSetter<String, Spinner> SETTER_SPINNER_CONTAINT = new FieldValueSetter<String, Spinner>() {

		@Override
		public boolean setValue(String entity, Spinner spinner) {
			return false;
		}
	};

	final static FieldValueSetter<Object, View> DEFAULT_SETTER = new FieldValueSetter<Object, View>() {
		@Override
		public boolean setValue(Object value, View v) {
			try {
				if (v instanceof TextView) {
					TextView t = (TextView) v;
					t.setText(FormTools.parseString(value));
					return true;
				} else if (v instanceof CheckBox) {
					CheckBox t = (CheckBox) v;
					t.setChecked(FormTools.parseBoolean(value));
					return true;
				} else if (v instanceof Spinner) {
					Spinner t = (Spinner) v;
					t.setSelection(FormTools.parseInt(value));
					return true;
				} else if (v instanceof RadioButton) {
					RadioButton t = (RadioButton) v;
					t.setChecked(FormTools.parseBoolean(value));
					return true;
				} else if (v instanceof RadioGroup) {
					RadioButton t = (RadioButton) v;
					t.setChecked(FormTools.parseBoolean(value));
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}
	};

	@Override
	protected final FieldValueGetSetter getDefaultHandler() {
		return DEFAULT_SETTER;
	}

	protected final void traitViewAsViewGroup(ViewGroup v) {
		String[] fields = form.getFieldNames();
		for (String field : fields) {
			View view = v.findViewWithTag(field);
			if (view != null) {
				if (view instanceof ViewGroup) {
					mutateView(view);
				} else {
					traitViewAsSimpleView(view);
				}
			}
		}
	}
}
