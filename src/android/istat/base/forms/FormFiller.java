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

	List<FieldModel> fieldModels = new ArrayList<FieldModel>();

	private void fillFormFieldWithView(View v, String fieldName) {
		if (fieldModels != null && fieldModels.size() > 0) {
			for (FieldModel model : fieldModels) {
				boolean result = model.onModelling(form, v.getTag() + "", v);
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
		return fillFromView(form, view, editableOnly, new FieldModel[0]);
	}

	public static FormFiller fillFromView(Form form, View view,
			boolean editableOnly, FieldModel... models) {
		return fillFromView(form, view, editableOnly, models);
	}

	public static FormFiller fillFromView(Form form, View view,
			FieldModel... models) {
		return fillFromView(form, view, false,
				models != null ? Arrays.asList(models) : null);
	}

	public static FormFiller fillFromView(Form form, View view,
			boolean editableOnly, List<FieldModel> models) {
		FormFiller filler = new FormFiller(form);
		filler.addFieldModels(models);
		filler.fillFormUsingAutoMatchedTargetedView(view, editableOnly);
		return filler;
	}

	void addFieldModels(List<FieldModel> models) {
		fieldModels = models != null ? models : new ArrayList<FieldModel>();
		fieldModels.add(defaultModel);
	}

	FieldModel defaultModel = new FieldModel() {

		@Override
		public boolean onModelling(Form form, String fieldName, View v) {
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
}
