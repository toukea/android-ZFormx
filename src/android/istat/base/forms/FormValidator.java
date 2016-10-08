package android.istat.base.forms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.json.JSONObject;
import android.view.View;

public final class FormValidator {
	FormState formState;
	HashMap<String, List<FieldValidator>> validationCondition = new HashMap<String, List<FieldValidator>>();
	ValidationListner validationListener;

	public void setValidationListener(ValidationListner validationListener) {
		this.validationListener = validationListener;
	}

	public final static FormState validate(Form form, View formView,
			FormValidationCondition condition) {
		return validate(form, formView, condition.getConditionMap());
	}

	public final static FormState validate(Form form, View formView,
			HashMap<String, List<FieldValidator>> conditions) {
		FormValidator validator = new FormValidator();
		validator.setValidationCondition(conditions);
		return validator.validate(form, formView);
	}

	public void setValidationCondition(FormValidationCondition condition) {
		setValidationCondition(condition.getConditionMap());
	}

	public void setValidationCondition(
			HashMap<String, List<FieldValidator>> validationController) {
		if (validationController == null) {
			return;
		}
		this.validationCondition = validationController;
	}

	public FormValidator addCondition(String field, FieldValidator validator) {
		List<FieldValidator> validators = validationCondition.get(field);
		if (validators == null) {
			validators = new ArrayList<FieldValidator>();
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
			List<FieldValidator> validators = validationCondition.get(key);
			Object objValue = form.get(key);
			FormFieldError error = null;
			for (FieldValidator validator : validators) {
				boolean isValidated = validator.validate(form, key, objValue);
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
		protected final HashMap<String, List<FieldValidator>> conditionMap = new HashMap<String, List<FieldValidator>>();

		public FormValidationCondition() {

		}

		public FormValidationCondition(
				HashMap<String, List<FieldValidator>> conditionMap) {
			this.conditionMap.putAll(conditionMap);
		}

		public FormValidationCondition setFieldValidators(String fieldName,
				List<FieldValidator> validators) {
			conditionMap.put(fieldName, validators);
			return this;
		}

		public List<FieldValidator> getFieldValidators(String fieldName) {
			return conditionMap.get(fieldName);
		}

		protected HashMap<String, List<FieldValidator>> getConditionMap() {
			return conditionMap;
		}

		public JSONObject toJson() {
			return null;

		}

		public final static FormValidationCondition fromJson(JSONObject json) {
			return null;

		}
	}

	public static abstract class FieldValidator {
		boolean breakValidationIfError = false;

		public void setBreakValidationIfError(boolean breakIfError) {
			this.breakValidationIfError = breakIfError;
		}

		public boolean hasBreakValidationIfErrorEnable() {
			return breakValidationIfError;
		}

		public abstract String getErrorMessage();

		public final boolean validate(Form form, String fieldName, Object value) {
			return onValidate(form, fieldName, value);
		}

		protected abstract boolean onValidate(Form form, String fieldName,
				Object value);

		public JSONObject toJson() {
			JSONObject json = new JSONObject();
			return json;
		}

		public FieldValidator fillFrom(JSONObject json) {
			return null;
		}
	}

	public static interface ValidationListner {
		public void onValidationStarting(Form form, View formView);

		public void onValidateField(Form form, String FieldName, Object value,
				View viewCause, FormValidator.FieldValidator validator,
				boolean validated);

		public void onValidationCompleted(Form form, View formView,
				FormState state);
	}
}
