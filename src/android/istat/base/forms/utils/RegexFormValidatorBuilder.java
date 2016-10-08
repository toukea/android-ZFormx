package android.istat.base.forms.utils;

import android.istat.base.forms.FormValidator;

public class RegexFormValidatorBuilder {
	RegexFormConditionBuilder conditionBuilder = new RegexFormConditionBuilder();

	public FormValidator create() {
		FormValidator validator = new FormValidator();
		validator.setValidationCondition(conditionBuilder.create());
		return validator;
	}
}
