package android.istat.base.forms.utils;

import java.util.ArrayList;
import java.util.List;

import android.istat.base.forms.FormValidator;
import android.istat.base.forms.FormValidator.FieldValidator;

public final class RegexFormValidatorBuilder {
	RegexValidationDirectiveBuilder conditionBuilder = new RegexValidationDirectiveBuilder();

	public RegexFormValidatorBuilder addFieldValidationParams(String fieldName,
			String regexCondition, String message) {
		conditionBuilder.addFieldValidationParams(fieldName, regexCondition,
				message);
		return this;
	}

	public RegexFormValidatorBuilder setFieldValidationParams(String fieldName,
			String regexCondition, String message) {
		conditionBuilder.setFieldValidationParams(fieldName, regexCondition,
				message);
		return this;
	}

	public RegexFormValidatorBuilder setFieldValidator(String fieldName,
			RegexFieldValidator validator) {
		conditionBuilder.setFieldValidator(fieldName, validator);
		return this;
	}

	public RegexFormValidatorBuilder addFieldValidator(String fieldName,
			RegexFieldValidator validator) {
		conditionBuilder.addFieldValidator(fieldName, validator);
		return this;
	}

	public RegexFormValidatorBuilder addAllFieldValidator(String fieldName,
			List<RegexFieldValidator> validator) {
		conditionBuilder.addAllFieldValidator(fieldName, validator);
		return this;
	}

	public RegexFormValidatorBuilder setFieldValidators(String fieldName,
			List<RegexFieldValidator> regexValidators) {
		List<FieldValidator> validators = new ArrayList<FormValidator.FieldValidator>();
		validators.addAll(regexValidators);
		conditionBuilder.setFieldValidators(fieldName, validators);
		return this;
	}

	public FormValidator create() {
		FormValidator validator = new FormValidator();
		validator.setValidationDirective(conditionBuilder.create());
		return validator;
	}
}
