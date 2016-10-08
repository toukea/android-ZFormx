package android.istat.base.forms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.json.JSONObject;

import android.text.TextUtils;
import android.view.View;

public final class FormValidator {
	FormState formState;
	HashMap<String, List<Validator>> validationCondition = new HashMap<String, List<Validator>>();
	ValidationListner validationListener;

	public void setValidationListener(ValidationListner validationListener) {
		this.validationListener = validationListener;
	}

	public final static FormState validate(Form form, View formView,
			FormValidationCondition condition) {
		return validate(form, formView, condition.getConditionMap());
	}

	public final static FormState validate(Form form, View formView,
			HashMap<String, List<Validator>> conditions) {
		FormValidator validator = new FormValidator();
		validator.setValidationCondition(conditions);
		return validator.validate(form, formView);
	}

	public void setValidationCondition(FormValidationCondition condition) {
		setValidationCondition(condition.getConditionMap());
	}

	public void setValidationCondition(
			HashMap<String, List<Validator>> validationController) {
		if (validationController == null) {
			return;
		}
		this.validationCondition = validationController;
	}

	public FormValidator addCondition(String field, Validator validator) {
		List<Validator> validators = validationCondition.get(field);
		if (validators == null) {
			validators = new ArrayList<Validator>();
		}
		validators.add(validator);
		validationCondition.put(field, validators);
		return this;
	}

	public FormState validate(Form form, View formView) {
		FormState state = new FormState();
		proceedCheckup(form, state, formView);
		return state;
	}

	private void proceedCheckup(Form form, FormState state, View formView) {
		Iterator<String> iterator = form.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			List<Validator> validators = validationCondition.get(key);
			Object objValue = form.get(key);
			FormFieldError error = null;
			for (Validator validator : validators) {
				boolean isValidated = validator.onValidate(form, key, objValue);
				if (!isValidated) {
					if (error == null) {
						error = new FormFieldError(key, objValue);
					}
					error.viewCause = formView.findViewWithTag(key);
					error.addFailedValidators(validator);
					state.addError(error);
					if (validator.hasBreakValidationIfErrorEnable()) {
						break;
					}
				}
			}
			if (error != null) {
				state.addError(error);
			}
		}
	}

	public static class FormValidationCondition {
		final HashMap<String, List<Validator>> conditionMap = new HashMap<String, List<Validator>>();

		public FormValidationCondition() {

		}

		public FormValidationCondition(
				HashMap<String, List<Validator>> conditionMap) {
			this.conditionMap.putAll(conditionMap);
		}

		// public FormValidationCondition addFieldValidator(String fieldName,
		// Validator validator) {
		// List<Validator> validators = conditionMap.get(fieldName);
		// if (validators == null) {
		// validators = new ArrayList<Validator>();
		// }
		// validators.add(validator);
		// conditionMap.put(fieldName, validators);
		// return this;
		// }
		//
		// public FormValidationCondition addFieldValidationParams(
		// String fieldName, String regexCondition, String message) {
		// List<Validator> validators = conditionMap.get(fieldName);
		// if (validators == null) {
		// validators = new ArrayList<Validator>();
		// }
		// Validator validator = new Validator(regexCondition, message);
		// validators.add(validator);
		// conditionMap.put(fieldName, validators);
		// return this;
		// }
		//
		// public FormValidationCondition setFieldValidationParams(
		// String fieldName, String regexCondition, String message) {
		// List<Validator> validators = new ArrayList<Validator>();
		// Validator validator = new Validator(regexCondition, message);
		// validators.add(validator);
		// conditionMap.put(fieldName, validators);
		// return this;
		// }

		public FormValidationCondition setFieldValidators(String fieldName,
				List<Validator> validators) {
			conditionMap.put(fieldName, validators);
			return this;
		}

		public List<Validator> getFieldValidators(String fieldName) {
			return conditionMap.get(fieldName);
		}

		HashMap<String, List<Validator>> getConditionMap() {
			return conditionMap;
		}

		public JSONObject toJson() {
			return null;

		}

		public final static FormValidationCondition fromJson(JSONObject json) {
			return null;

		}
	}

	public static abstract class Validator {
		boolean breakValidationIfError = false;

		public void setBreakValidationIfError(boolean breakIfError) {
			this.breakValidationIfError = breakIfError;
		}

		public boolean hasBreakValidationIfErrorEnable() {
			return breakValidationIfError;
		}

		public abstract String getMessage();

		protected abstract boolean onValidate(Form form, String fieldName,
				Object value);

	}

	public static interface ValidationListner {
		public void onValidationStarting(Form form, View formView);

		public void onValidateField(Form form, String FieldName, Object value,
				View viewCause, FormValidator.Validator validator,
				boolean validated);

		public void onValidationCompleted(Form form, View formView,
				FormState state);
	}
}
