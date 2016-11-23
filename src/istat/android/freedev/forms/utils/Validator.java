package istat.android.freedev.forms.utils;

import android.view.View;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import istat.android.freedev.forms.Form;
import istat.android.freedev.forms.FormState;

/**
 * Not Yet used for now.
 *
 * @author istat
 */
abstract class Validator {
    HashMap<String, List<FieldValidator>> constraints = new HashMap<String, List<FieldValidator>>();
    ValidationListener validationListener;

    public abstract FormState validate();

    public final Validator setValidationListener(ValidationListener validationListener) {
        this.validationListener = validationListener;
        return this;
    }

    public final void setConstraints(
            HashMap<String, List<FieldValidator>> validationDirective) {
        if (validationDirective == null) {
            this.constraints.clear();
            return;
        }
        this.constraints = validationDirective;
    }

    public final Validator addConstraint(String field, FieldValidator validator) {
        List<FieldValidator> validators = constraints.get(field);
        if (validators == null) {
            validators = new ArrayList<FieldValidator>();
        }
        validators.add(validator);
        constraints.put(field, validators);
        return this;
    }

    private void proceedCheckup(Form form, FormState state, View formView) {
        if (validationListener != null) {
            validationListener.onValidationStarting(form, formView);
        }
        Iterator<String> iterator = form.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            List<FieldValidator> validators = constraints.get(key);
            Object objValue = form.get(key);
            for (FieldValidator validator : validators) {
                boolean isValidated = validator.validate(form, key, objValue);
                if (!isValidated) {
                    onValidationError(state, key, objValue, formView.findViewWithTag(key), validator);
                    if (validationListener != null) {
                        validationListener.onValidateField(form, key, objValue,
                                formView, validator, isValidated);
                    }
                    if (validator.hasBreakValidationIfErrorEnable()) {
                        break;
                    }
                }
            }
        }
        if (validationListener != null) {
            validationListener.onValidationCompleted(form, formView, state);
        }
    }

    abstract void onValidationError(FormState state, String fieldName, Object fieldValue, View viewCause, FieldValidator validator);

    public static abstract class FieldValidator {
        final String FIELD_BREAK_IF_ERROR = "breakValidationIfError";
        final String FIELD_ERROR_MESSAGE = "errorMessage";
        boolean breakValidationIfError = false;

        public FieldValidator() {

        }

        public FieldValidator(boolean breakValidationIfError) {
            this.breakValidationIfError = breakValidationIfError;
        }

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
            try {
                json.put(FIELD_BREAK_IF_ERROR, breakValidationIfError);
                json.put(FIELD_ERROR_MESSAGE, getErrorMessage());
            } catch (Exception e) {

            }
            return json;
        }

        public FieldValidator fillFrom(JSONObject json) {
            this.breakValidationIfError = json.optBoolean(FIELD_BREAK_IF_ERROR);
            return this;
        }
    }

    public static interface ValidationListener {
        public void onValidationStarting(Form form, View formView);

        public void onValidateField(Form form, String FieldName, Object value,
                                    View viewCause, Validator.FieldValidator validator,
                                    boolean validated);

        public void onValidationCompleted(Form form, View formView,
                                          FormState state);
    }
}
