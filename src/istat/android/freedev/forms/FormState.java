package istat.android.freedev.forms;

import android.text.TextUtils;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * @author istat
 */
public final class FormState {
	List<FormFieldError> errors = new ArrayList<FormFieldError>();
	Form form;

	FormState(Form form) {
		this.form = form;
	}

	public FormState() {

	}

	public List<FormFieldError> getErrors() {
		return errors;
	}

	public boolean hasError() {
		return errors != null && !errors.isEmpty();
	}

	FormState addError(FormFieldError error) {
		errors.add(error);
		return this;
	}

	public Form getForm() {
		return form;
	}

	public List<FormFieldError> getErrorByViewCause(View cause) {
		List<FormFieldError> errors = new ArrayList<FormFieldError>();
		for (FormFieldError error : getErrors()) {
			if (cause != null && cause.equals(error.getViewCause())) {
				errors.add(error);
			}
		}
		return errors;
	}

	public List<FormFieldError> getErrorByFieldName(String fieldName) {
		List<FormFieldError> errors = new ArrayList<FormFieldError>();
		for (FormFieldError error : getErrors()) {
			if (!TextUtils.isEmpty(fieldName)
					&& fieldName.equals(error.getFieldValue())) {
				errors.add(error);
			}
		}
		return errors;
	}
}
