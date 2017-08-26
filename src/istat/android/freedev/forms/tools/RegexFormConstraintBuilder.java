package istat.android.freedev.forms.tools;

import java.util.ArrayList;
import java.util.List;
import istat.android.freedev.forms.FormValidator.FieldValidator;
import istat.android.freedev.forms.utils.FormConstraintBuilder;

public final class RegexFormConstraintBuilder extends FormConstraintBuilder {
    
	public RegexFormConstraintBuilder() {

	}

	public RegexFormConstraintBuilder appendFieldValidationParams(
			String fieldName, String regexCondition, String message) {
		return appendFieldValidationParams(fieldName, regexCondition, message,
				false);
	}

	public RegexFormConstraintBuilder applyFieldValidationParams(
			String fieldName, String regexCondition, String message) {
		return applyFieldValidationParams(fieldName, regexCondition, message,
				false);
	}

	public RegexFormConstraintBuilder appendFieldValidationParams(
			String fieldName, String regexCondition, String message,
			boolean breakIfError) {
		List<FieldValidator> validators = conditionMap.get(fieldName);
		if (validators == null) {
			validators = new ArrayList<FieldValidator>();
		}
		FieldValidator validator = new RegexFieldValidator(regexCondition,
				message);
		validator.setBreakValidationIfError(breakIfError);
		validators.add(validator);
		conditionMap.put(fieldName, validators);
		return this;
	}

	public RegexFormConstraintBuilder applyFieldValidationParams(
			String fieldName, String regexCondition, String message,
			boolean breakIfError) {
		List<FieldValidator> validators = new ArrayList<FieldValidator>();
		FieldValidator validator = new RegexFieldValidator(regexCondition,
				message);
		validator.setBreakValidationIfError(breakIfError);
		validators.add(validator);
		conditionMap.put(fieldName, validators);
		return this;
	}
}
