package android.istat.base.forms.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.istat.base.forms.FormValidator.FormValidationCondition;
import android.istat.base.forms.FormValidator.FieldValidator;

public class RegexFormConditionBuilder {
	RegexFormValidationCondition condition = new RegexFormValidationCondition();

	public RegexFormConditionBuilder addFieldValidator(String fieldName,
			FieldValidator validator) {
		List<FieldValidator> validators = condition.getConditionMap().get(
				fieldName);
		if (validators == null) {
			validators = new ArrayList<FieldValidator>();
		}
		validators.add(validator);
		condition.getConditionMap().put(fieldName, validators);
		return this;
	}

	public RegexFormConditionBuilder addFieldValidationParams(String fieldName,
			String regexCondition, String message) {
		List<FieldValidator> validators = condition.getConditionMap().get(
				fieldName);
		if (validators == null) {
			validators = new ArrayList<FieldValidator>();
		}
		FieldValidator validator = new RegexFieldValidator(regexCondition,
				message);
		validators.add(validator);
		condition.getConditionMap().put(fieldName, validators);
		return this;
	}

	public RegexFormConditionBuilder setFieldValidationParams(String fieldName,
			String regexCondition, String message) {
		List<FieldValidator> validators = new ArrayList<FieldValidator>();
		FieldValidator validator = new RegexFieldValidator(regexCondition,
				message);
		validators.add(validator);
		condition.getConditionMap().put(fieldName, validators);
		return this;
	}

	public FormValidationCondition create() {
		return condition;
	}

	class RegexFormValidationCondition extends FormValidationCondition {
		@Override
		public HashMap<String, List<FieldValidator>> getConditionMap() {
			// TODO Auto-generated method stub
			return super.getConditionMap();
		}
	}

}
