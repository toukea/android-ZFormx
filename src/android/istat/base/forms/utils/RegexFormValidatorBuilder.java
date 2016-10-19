package android.istat.base.forms.utils;

import java.util.ArrayList;
import java.util.List;

import android.istat.base.forms.FormValidator;
import android.istat.base.forms.FormValidator.FieldValidator;

public final class RegexFormValidatorBuilder {
	RegexFormConstraintBuilder conditionBuilder = new RegexFormConstraintBuilder();

	public final static RegexFormValidatorBuilder newInstance() {
		return new RegexFormValidatorBuilder();
	}

	private RegexFormValidatorBuilder() {

	}

	public RegexFormValidatorBuilder appendFieldValidationParams(
			String fieldName, String regexCondition, String message) {
		conditionBuilder.appendFieldValidationParams(fieldName, regexCondition,
				message);
		return this;
	}

	public RegexFormValidatorBuilder applyFieldValidationParams(
			String fieldName, String regexCondition, String message) {
		conditionBuilder.applyFieldValidationParams(fieldName, regexCondition,
				message);
		return this;
	}

	public RegexFormValidatorBuilder applyFieldValidator(String fieldName,
			RegexFieldValidator validator) {
		conditionBuilder.applyFieldValidator(fieldName, validator);
		return this;
	}

	public RegexFormValidatorBuilder appendFieldValidator(String fieldName,
			RegexFieldValidator validator) {
		conditionBuilder.appendFieldValidator(fieldName, validator);
		return this;
	}

	public RegexFormValidatorBuilder appendAllFieldValidator(String fieldName,
			List<RegexFieldValidator> validator) {
		conditionBuilder.appendAllFieldValidator(fieldName, validator);
		return this;
	}

	public RegexFormValidatorBuilder applyFieldValidators(String fieldName,
			List<RegexFieldValidator> regexValidators) {
		List<FieldValidator> validators = new ArrayList<FormValidator.FieldValidator>();
		validators.addAll(regexValidators);
		conditionBuilder.applyFieldValidators(fieldName, validators);
		return this;
	}

	public FormValidator create() {
		FormValidator validator = new FormValidator();
		validator.setValidationDirective(conditionBuilder.create());
		return validator;
	}
}
