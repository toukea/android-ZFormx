package istat.android.freedev.forms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;

import android.view.View;

/**
 * @author istat
 */
public class FormValidator {
    HashMap<String, List<FieldValidator>> constraints = new HashMap<String, List<FieldValidator>>();
    ValidationListener validationListener;
    Form form;
    View view;

    private FormValidator() {

    }

    public static FormValidator from(Form form) {
        FormValidator validator = new FormValidator();
        validator.form = form;
        return validator;
    }

    public static FormValidator from(View view) {
        FormValidator validator = new FormValidator();
        validator.view = view;
        return validator;
    }

    public static FormValidator from(Form form, View view) {
        FormValidator validator = new FormValidator();
        validator.view = view;
        validator.form = form;
        return validator;
    }

    public final FormState validate() {
        return validate(form, view);
    }

    public final FormValidator setValidationListener(ValidationListener validationListener) {
        this.validationListener = validationListener;
        return this;
    }

    public final static FormState validate(Form form,
                                           HashMap<String, List<FieldValidator>> constraints) {
        return validate(form, constraints);
    }

    public final static FormState validate(Form form,
                                           HashMap<String, List<FieldValidator>> constraints, ValidationListener listener) {
        return validate(form, null, constraints, listener);
    }

    public final static FormState validate(View formView,
                                           HashMap<String, List<FieldValidator>> constraints) {
        FormFiller.FillerPolicy nullFillerPolicy = null;
        return validate(formView, nullFillerPolicy, constraints, null);
    }

    public final static FormState validate(View formView, FormFiller.FillerPolicy fillerPolicy,
                                           HashMap<String, List<FieldValidator>> constraints, ValidationListener listener) {
        FormValidator validator = new FormValidator();
        validator.setConstraints(constraints);
        validator.setValidationListener(listener);
        return validator.validate(formView, fillerPolicy);
    }

    public final static FormState validate(Form form, View formView,
                                           HashMap<String, List<FieldValidator>> constraints) {
        FormValidator validator = new FormValidator();
        validator.setConstraints(constraints);
        return validator.validate(form, formView);
    }

    public final static FormState validate(Form form, View formView,
                                           HashMap<String, List<FieldValidator>> constraints, ValidationListener listener) {
        FormValidator validator = new FormValidator();
        validator.setConstraints(constraints);
        validator.setValidationListener(listener);
        return validator.validate(form, formView);
    }

    public final void setConstraints(
            HashMap<String, List<FieldValidator>> validationDirective) {
        if (validationDirective == null) {
            this.constraints.clear();
            return;
        }
        this.constraints = validationDirective;
    }

    public final FormValidator addConstraint(String field, FieldValidator validator) {
        List<FieldValidator> validators = constraints.get(field);
        if (validators == null) {
            validators = new ArrayList<FieldValidator>();
        }
        validators.add(validator);
        constraints.put(field, validators);
        return this;
    }


    final FormState validate(Form form, View formView) {
        FormState state = new FormState(form);
        proceedCheckup(form, state, formView);
        return state;
    }

//    final FormState validate(Form form) {
//        View nullView = null;
//        return validate(form, nullView);
//    }
//
//    final FormState validate(View formView) {
//        FormFiller.FillerPolicy policy = null;
//        return validate(formView, policy);
//    }

    final FormState validate(View formView, FormFiller.FillerPolicy policy) {
        Form form = new Form();
        FormFiller.fillWithView(form, formView, policy);
        return validate(form, formView);
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
            FormFieldError error = null;
            if (validators != null) {
                for (FieldValidator validator : validators) {
                    boolean isValidated = validator.validate(form, key, objValue);
                    if (!isValidated) {
                        if (error == null) {
                            error = new FormFieldError(key, objValue);
                        }
                        if (formView != null) {
                            error.viewCause = formView.findViewWithTag(key);
                        }
                        error.addFailedValidators(validator);
                        state.addError(error);
                        if (validationListener != null) {
                            validationListener.onValidateField(form, key, objValue,
                                    error.viewCause, validator, isValidated);
                        }
                        if (validator.hasBreakValidationIfErrorEnable()) {
                            break;
                        }
                    }
                }
            }
            if (error != null) {
                state.addError(error);
            }
        }
        if (validationListener != null) {
            validationListener.onValidationCompleted(form, formView, state);
        }
    }

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
                                    View viewCause, FormValidator.FieldValidator validator,
                                    boolean validated);

        public void onValidationCompleted(Form form, View formView,
                                          FormState state);
    }
}
