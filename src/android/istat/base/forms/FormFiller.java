package android.istat.base.forms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.istat.base.forms.interfaces.FieldHandler;
import android.istat.base.forms.utils.ViewUtil;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
/**
 * @author istat
 */
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

	List<FieldHandler> fieldHandlers = new ArrayList<FieldHandler>();

	private void fillFormFieldWithView(View v, String fieldName) {
		if (fieldHandlers != null && fieldHandlers.size() > 0) {
			for (FieldHandler model : fieldHandlers) {
				boolean result = model.onHandle(form, v.getTag() + "", v);
				if (result) {
					return;
				}
			}
		}
		throw new RuntimeException("unsuported view for form autoBind::"
				+ v.getClass());

	}

	private void onFillFromTextView(View v, String fieldName) {
		TextView t = (TextView) v;
		form.put(fieldName, t.getText().toString());
	}

	private void onFillFromCheckBox(View v, String fieldName) {
		CheckBox t = (CheckBox) v;
		form.put(fieldName, t.isChecked() + "");
	}

	private void onFillFromSpinner(View v, String fieldName) {
		Spinner t = (Spinner) v;
		form.put(fieldName, t.getSelectedItemPosition());
	}

	private void onFillFromRadioButton(View v, String fieldName) {
		RadioButton t = (RadioButton) v;
		form.put(fieldName, t.isChecked() + "");
	}

	public static FormFiller fillFromView(Form form, View view) {
		return fillFromView(form, view, false);
	}

	public static FormFiller fillFromView(Form form, View view,
			boolean editableOnly) {
		return fillFromView(form, view, editableOnly, new FieldHandler[0]);
	}

	public static FormFiller fillFromView(Form form, View view,
			boolean editableOnly, FieldHandler... handlers) {
		return fillFromView(form, view, editableOnly, handlers != null
				&& handlers.length > 0 ? Arrays.asList(handlers) : null);
	}

	public static FormFiller fillFromView(Form form, View view,
			FieldHandler... handlers) {
		return fillFromView(form, view, false, handlers != null
				&& handlers.length > 0 ? Arrays.asList(handlers) : null);
	}

	public static FormFiller fillFromView(Form form, View view,
			boolean editableOnly, List<FieldHandler> handlers) {
		FormFiller filler = new FormFiller(form);
		filler.addFieldhandlers(handlers);
		filler.fillFormUsingAutoMatchedTargetedView(view, editableOnly);
		return filler;
	}

	void addFieldhandlers(List<FieldHandler> handlers) {
		FieldHandler defaultModel = new FieldHandler() {

			@Override
			public boolean onHandle(Form form, String fieldName, View v) {
				try {
					if (v instanceof TextView) {
						onFillFromTextView(v, fieldName);
						return true;
					} else if (v instanceof CheckBox) {
						onFillFromCheckBox(v, fieldName);
						return true;
					} else if (v instanceof Spinner) {
						onFillFromSpinner(v, fieldName);
						return true;
					} else if (v instanceof RadioButton) {
						onFillFromRadioButton(v, fieldName);
						return true;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return false;
			}
		};
		fieldHandlers = handlers != null ? handlers : new ArrayList<FieldHandler>();
		fieldHandlers.add(defaultModel);
	}

}
