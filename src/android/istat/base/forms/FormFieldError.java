package android.istat.base.forms;

import android.istat.base.forms.FormValidator.Validator;
import android.text.TextUtils;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public final class FormFieldError {
	View viewCause;
	String fieldName;
	Object fieldValue;
	final List<FormValidator.Validator> failedValidators = new ArrayList<FormValidator.Validator>();

	FormFieldError(String name, Object value) {
		this.fieldName = name;
		this.fieldValue = value;
	}

	FormFieldError(String name, Object value,
			List<FormValidator.Validator> failedValidators) {
		this.fieldName = name;
		this.fieldValue = value;
		this.failedValidators.addAll(failedValidators);
	}

	FormFieldError() {
	}

	public List<String> getMessages() {
		List<String> messages = new ArrayList<String>();
		for (Validator validator : failedValidators) {
			messages.add(validator.getMessage());
		}
		return messages;
	}

	public String getFirstMessage() {
		for (Validator validator : failedValidators) {
			String message = validator.getMessage();
			if (!TextUtils.isEmpty(message)) {
				return message;
			}
		}
		return "";
	}

	public String getMessage(int index) {
		int count = 0;
		for (Validator validator : failedValidators) {
			String message = validator.getMessage();
			if (!TextUtils.isEmpty(message)) {
				if (count == index) {
					return message;
				}
				count++;
			}
		}
		return "";
	}

	public String getMessageAtIndexs(int index) {
		if (index < failedValidators.size()) {
			return failedValidators.get(index).getMessage();
		}
		return null;
	}

	public boolean hasViewCause() {
		return viewCause != null;
	}

	public Object getFieldValue() {
		return fieldValue;
	}

	public View getViewCause() {
		return viewCause;
	}

	public List<FormValidator.Validator> getFailedValidators() {
		return failedValidators;
	}

	public String getFieldName() {
		return fieldName;
	}

	void addFailedValidators(FormValidator.Validator validator) {
		failedValidators.add(validator);
	}
}
