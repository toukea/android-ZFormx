package android.istat.base.forms;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

public final class FormError {
    View viewCause;
    String fieldName;
    String fieldValue;
    final List<FormValidator.Validator> failedValidators = new ArrayList<FormValidator.Validator>();

    FormError(String name, String value, String condition, String message) {
        this.fieldName = name;
        this.fieldValue = value;
        this.failedValidators.add(new FormValidator.Validator(condition, message));
    }

    FormError(String name, String value, List<FormValidator.Validator> failedValidators) {
        this.fieldName = name;
        this.fieldValue = value;
        this.failedValidators.addAll(failedValidators);
    }

    FormError(String name, String value, String condition) {
        this.fieldName = name;
        this.fieldValue = value;
        this.failedValidators.add(new FormValidator.Validator(condition, ""));
    }

    FormError() {
    }

    public boolean hasViewCause() {
        return viewCause != null;
    }

    public String getFieldValue() {
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
