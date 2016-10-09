package android.istat.base.forms.utils;

import java.util.ArrayList;
import java.util.List;
import android.istat.base.forms.FormValidator.FieldValidator;

public final class RegexValidationDirectiveBuilder extends ValidationDirectiveBuilder {

	public RegexValidationDirectiveBuilder appendFieldValidationParams(
			String fieldName, String regexCondition, String message) {
		List<FieldValidator> validators = conditionMap.get(fieldName);
		if (validators == null) {
			validators = new ArrayList<FieldValidator>();
		}
		FieldValidator validator = new RegexFieldValidator(regexCondition,
				message);
		validators.add(validator);
		conditionMap.put(fieldName, validators);
		return this;
	}

	public RegexValidationDirectiveBuilder applyFieldValidationParams(
			String fieldName, String regexCondition, String message) {
		List<FieldValidator> validators = new ArrayList<FieldValidator>();
		FieldValidator validator = new RegexFieldValidator(regexCondition,
				message);
		validators.add(validator);
		conditionMap.put(fieldName, validators);
		return this;
	}

}
