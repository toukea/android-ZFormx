package istat.android.freedev.forms.tools;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

import istat.android.freedev.forms.Form;
import istat.android.freedev.forms.FormValidator;
import istat.android.freedev.forms.FormValidator.FieldValidator;

class RegexFormAbsValidatorBuilder {
    protected RegexFormConstraintBuilder conditionBuilder = new RegexFormConstraintBuilder();

    @Deprecated
    public final static RegexFormAbsValidatorBuilder newInstance() {
        return new RegexFormAbsValidatorBuilder();
    }

    public RegexFormAbsValidatorBuilder appendFieldValidationParams(
            String fieldName, String regexCondition, String message) {
        conditionBuilder.appendFieldValidationParams(fieldName, regexCondition,
                message);
        return this;
    }

    public RegexFormAbsValidatorBuilder applyFieldValidationParams(
            String fieldName, String regexCondition, String message) {
        conditionBuilder.applyFieldValidationParams(fieldName, regexCondition,
                message);
        return this;
    }

    public RegexFormAbsValidatorBuilder applyFieldValidator(String fieldName,
                                                            RegexFieldValidator validator) {
        conditionBuilder.applyFieldValidator(fieldName, validator);
        return this;
    }

    public RegexFormAbsValidatorBuilder appendFieldValidator(String fieldName,
                                                             RegexFieldValidator validator) {
        conditionBuilder.appendFieldValidator(fieldName, validator);
        return this;
    }

    public RegexFormAbsValidatorBuilder appendAllFieldValidator(String fieldName,
                                                                List<RegexFieldValidator> validator) {
        conditionBuilder.appendAllFieldValidator(fieldName, validator);
        return this;
    }

    public RegexFormAbsValidatorBuilder applyFieldValidators(String fieldName,
                                                             List<RegexFieldValidator> regexValidators) {
        List<FieldValidator> validators = new ArrayList<FieldValidator>();
        validators.addAll(regexValidators);
        conditionBuilder.applyFieldValidators(fieldName, validators);
        return this;
    }

//    public FormValidator create(Form form) {
//        FormValidator validator = FormValidator.from(form);
//        validator.setConstraints(conditionBuilder.create());
//        return validator;
//    }
//
//    public FormValidator create(Form form, View view) {
//        FormValidator validator = FormValidator.from(form, view);
//        validator.setConstraints(conditionBuilder.create());
//        return validator;
//    }
}
