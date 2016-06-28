package android.istat.base.forms;

import java.util.List;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

public class FormFiller {
	private Form form;

	FormFiller(Form form) {
		this.form = form;
	}

	void fillFormUsingViewIdMatching(View formBaseView, int[] viewIds) {
		String[] formFields = form.getFieldNames();
		int fieldSize = formFields.length;
		int idSize = viewIds.length;
		fieldSize = fieldSize < idSize ? fieldSize : idSize;
		for (int i = 0; i < fieldSize; i++) {
			View view = formBaseView.findViewById(viewIds[i]);
			if (view != null) {
				fillFormFieldWithView(view, formFields[i]);
			}
		}
	}

	void fillFormUsingAutoMatchedTargetedView(View formBaseView,
			boolean touchableOnly) {
		if (formBaseView != null) {
			if (formBaseView instanceof ViewGroup) {
				traitViewAsViewGroup((ViewGroup) formBaseView, touchableOnly);
			} else {
				traitViewAsSimpleView(formBaseView);
			}
		}
	}

	void fillFormUsingTargetedView(View formBaseView) {
		String[] formFields = form.getFieldNames();
		int fieldSize = formFields.length;
		for (int i = 0; i < fieldSize; i++) {
			View view = formBaseView.findViewWithTag(formFields[i]);
			if (view != null) {
				fillFormFieldWithView(view, formFields[i]);
			}
		}
	}

	// private List<View> getChillViews(View v) {
	//
	// if (!(v instanceof ViewGroup)) {
	// ArrayList<View> viewArrayList = new ArrayList<View>();
	// viewArrayList.add(v);
	// return viewArrayList;
	// }
	//
	// ArrayList<View> result = new ArrayList<View>();
	//
	// ViewGroup viewGroup = (ViewGroup) v;
	// for (int i = 0; i < viewGroup.getChildCount(); i++) {
	//
	// View child = viewGroup.getChildAt(i);
	//
	// ArrayList<View> viewArrayList = new ArrayList<View>();
	// viewArrayList.add(v);
	// viewArrayList.addAll(getChillViews(child));
	//
	// result.addAll(viewArrayList);
	// }
	// return result;
	// }

	private void traitViewAsViewGroup(ViewGroup v, boolean touchableOnly) {
		List<View> childV = !touchableOnly ? ViewUtil.getDirectChildViews(v)
				: v.getTouchables();
		for (View view : childV) {
			if (view instanceof ViewGroup) {
				fillFormUsingAutoMatchedTargetedView(view, touchableOnly);
			} else {
				traitViewAsSimpleView(view);
			}
		}

	}

	private void traitViewAsSimpleView(View v) {
		if (v.getTag() != null && !TextUtils.isEmpty(v.getTag() + "")) {
			fillFormFieldWithView(v, v.getTag() + "");
		}
	}

	private void fillFormFieldWithView(View v, String fieldName) {
		try {
			if (v instanceof TextView) {
				onFillFromTextView(v, fieldName);
			} else if (v instanceof CheckBox) {
				onFillFromCheckBox(v, fieldName);
			} else if (v instanceof Spinner) {
				onFillFromSpinner(v, fieldName);
			} else if (v instanceof RadioButton) {
				onFillFromRadioButton(v, fieldName);
			} else if (v instanceof RadioGroup) {
				onFillFromRadioGroup(v, fieldName);
			} else {
				if (formViewAutoBinder != null) {
					formViewAutoBinder.onAutoBindNoSupported(v, fieldName);
				}
				throw new RuntimeException(
						"unsuported view for form autoBind::" + v.getClass());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void onFillFromTextView(View v, String fieldName) {
		TextView t = (TextView) v;
		form.put(fieldName, t.getText().toString());
	}

	private void onFillFromCheckBox(View v, String fieldName) {
		TextView t = (TextView) v;
		form.put(fieldName, t.getText().toString());
	}

	private void onFillFromSpinner(View v, String fieldName) {
		TextView t = (TextView) v;
		form.put(fieldName, t.getText().toString());
	}

	private void onFillFromRadioButton(View v, String fieldName) {
		TextView t = (TextView) v;
		form.put(fieldName, t.getText().toString());
	}

	private void onFillFromRadioGroup(View v, String fieldName) {
		TextView t = (TextView) v;
		form.put(fieldName, t.getText().toString());
	}

	private FormViewAutoBinder formViewAutoBinder;

	// public void setFormViewAutoBinder(FormViewAutoBinder FormViewAutoBinder)
	// {
	// this.formViewAutoBinder = FormViewAutoBinder;
	// }

//	public static FormFiller fillFromBaseView(Form form, View view) {
//		FormFiller filler = new FormFiller(form);
//		filler.fillFormUsingTargetedView(view);
//		return filler;
//	}

//	public static FormFiller fillFromView(Form form, View view, int[] ids) {
//		FormFiller filler = new FormFiller(form);
//		filler.fillFormUsingViewIdMatching(view, ids);
//		return filler;
//	}

	public static FormFiller fillFromView(Form form, View view,
			FormViewAutoBinder formViewAutoBinder) {
		return fillFromView(form, view, false, formViewAutoBinder);
	}

	public static FormFiller fillFromView(Form form, View view) {
		return fillFromView(form, view, false);
	}

	public static FormFiller fillFromView(Form form, View view,
			boolean editableOnly) {
		return fillFromView(form, view, editableOnly, null);
	}

	public static FormFiller fillFromView(Form form, View view,
			boolean editableOnly, FormViewAutoBinder formViewAutoBinder) {
		FormFiller filler = new FormFiller(form);
		filler.formViewAutoBinder = formViewAutoBinder;
		filler.fillFormUsingAutoMatchedTargetedView(view, editableOnly);
		return filler;
	}

	public interface FormViewAutoBinder {
		public void onAutoBindNoSupported(View v, String fieldName);

	}
}
