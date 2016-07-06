package android.istat.base.forms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.istat.base.forms.interfaces.FieldModel;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

public class FormFlower {
	Form form;

	FormFlower(Form form) {
		this.form = form;
	}

	void flowIntoView(View formBaseView) {
		if (formBaseView != null) {
			if (formBaseView instanceof ViewGroup) {
				traitViewAsViewGroup((ViewGroup) formBaseView);
			} else {
				traitViewAsSimpleView(formBaseView);
			}
		}
	}

	private void traitViewAsViewGroup(ViewGroup v) {
		String[] fields = form.getFieldNames();
		for (String field : fields) {
			View view = v.findViewWithTag(field);
			if (view != null) {
				if (view instanceof ViewGroup) {
					flowIntoView(view);
				} else {
					traitViewAsSimpleView(view);
				}
			}
		}

	}

	private void traitViewAsSimpleView(View v) {
		if (v.getTag() != null && !TextUtils.isEmpty(v.getTag() + "")) {
			flowView(v);
		}
	}

	private void flowView(View v) {
		try {
			if (v instanceof TextView) {
				flowTextView(v);
			} else if (v instanceof CheckBox) {
				flowCheckBox(v);
			} else if (v instanceof Spinner) {
				flowSpinner(v);
			} else if (v instanceof RadioButton) {
				flowRadioButton(v);
			} else if (v instanceof RadioGroup) {
				flowRadioGroup(v);
			} else {
				if (formExtractor != null) {
					formExtractor.onFlow(v);
				}
				throw new RuntimeException(
						"unsuported view for form autoBind::" + v.getClass());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void flowTextView(View v) {
		TextView t = (TextView) v;
		String value = form.optString(v.getTag() + "");
		if (!TextUtils.isEmpty(value)) {
			t.setText(value);
		}
	}

	private void flowCheckBox(View v) {
		TextView t = (TextView) v;
		form.put(v.getTag() + "", t.getText().toString());
	}

	private void flowSpinner(View v) {
		TextView t = (TextView) v;
		form.put(v.getTag() + "", t.getText().toString());
	}

	private void flowRadioButton(View v) {
		TextView t = (TextView) v;
		form.put(v.getTag() + "", t.getText().toString());
	}

	private void flowRadioGroup(View v) {
		TextView t = (TextView) v;
		form.put(v.getTag() + "", t.getText().toString());
	}

	ViewFlower formExtractor;

	public void setViewFlower(ViewFlower flower) {
		this.formExtractor = flower;
	}

	public interface ViewFlower {
		public void onFlow(View v);

	}

	public static FormFlower flowIntoView(Form form, View view) {
		FormFlower binder = new FormFlower(form);
		binder.flowIntoView(view);
		return binder;
	}

	public static FormFlower flowIntoView(Form form, View view,
			boolean editableOnly) {
		FormFlower binder = new FormFlower(form);
		binder.flowIntoView(view);
		return binder;
	}

	// -----------------------
	List<FieldModel> fieldModels = new ArrayList<FieldModel>();

	public static FormFlower flowIntoView(Form form, View view,
			FieldModel... fieldModels) {
		FormFlower binder = new FormFlower(form);
		binder.fieldModels = Arrays.asList(fieldModels);
		binder.flowIntoView(view);
		return binder;
	}

	public static FormFlower flowIntoView(Form form, View view,
			boolean editableOnly, FieldModel... fieldModels) {

		FormFlower binder = new FormFlower(form);
		binder.fieldModels = Arrays.asList(fieldModels);
		binder.flowIntoView(view);
		return binder;
	}
}
