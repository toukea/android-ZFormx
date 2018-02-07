package istat.android.freedev.forms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;

/**
 * @author istat
 */
public class FormValidator {
    HashMap<String, List<FieldValidator>> constraints = new HashMap<String, List<FieldValidator>>();
    ValidationListener validationListener;
    Handler handler = new Handler(Looper.getMainLooper());
    FormFiller.FillerPolicy fillerPolicy;

    public FormValidator() {

    }

    public void setFillerPolicy(FormFiller.FillerPolicy fillerPolicy) {
        this.fillerPolicy = fillerPolicy;
    }
//    Form form;
//    View view;
//    public static FormValidator from(Form form) {
//        FormValidator validator = new FormValidator();
//        validator.form = form;
//        return validator;
//    }
//
//    public static FormValidator from(View view) {
//        FormValidator validator = new FormValidator();
//        validator.view = view;
//        return validator;
//    }
//
//    public static FormValidator from(Form form, View view) {
//        FormValidator validator = new FormValidator();
//        validator.view = view;
//        validator.form = form;
//        return validator;
//    }

//    public final FormState validate() {
//        if (fillerPolicy != null) {
//            return validate(form, view, fillerPolicy);
//        } else {
//            return validate(form, view);
//        }
//    }

    public final FormValidator setValidationListener(ValidationListener validationListener) {
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

    public final FormValidator addConstraint(String field, FieldValidator validator) {
        List<FieldValidator> validators = constraints.get(field);
        if (validators == null) {
            validators = new ArrayList<FieldValidator>();
        }
        validators.add(validator);
        constraints.put(field, validators);
        return this;
    }


    public final FormState validate(Form form) {
        View nullView = null;
        return validate(form, nullView);
    }

    public final FormState validate(View formView) {
        FormFiller.FillerPolicy policy = null;
        return validate(formView, policy);
    }

    public final FormState validate(Form form, View formView) {
        FormState state = new FormState(form);
        proceedCheckup(form, state, formView);
        return state;
    }

    public final FormState validate(View formView, FormFiller.FillerPolicy policy) {
        return validate(new Form(), formView, policy);
    }

    public final FormState validate(Form form, View formView, FormFiller.FillerPolicy policy) {
        FormFiller.fillWithView(form, formView, policy);
        return validate(form, formView);
    }

    private void proceedCheckup(final Form form, final FormState state, final View formView) {
        if (validationListener != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    validationListener.onValidationStarting(form, formView);
                }
            });
        }

        Iterator<String> iterator = form.keySet().iterator();
        String key;
        Object objValue;
        FormFieldError error;
        while (iterator.hasNext()) {
            key = iterator.next();
            List<FieldValidator> validators = constraints.get(key);
            objValue = form.get(key);
            error = null;
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
                            onValidateField(form, key, objValue,
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
            handler.post(new Runnable() {
                @Override
                public void run() {
                    validationListener.onValidationCompleted(form, formView, state);
                }
            });
        }
    }

    private void onValidateField(final Form form, final String key, final Object objValue, final View viewCause, final FieldValidator validator, final boolean isValidated) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                validationListener.onValidateField(form, key, objValue,
                        viewCause, validator, isValidated);
            }
        });
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

        public boolean hasErrorMessage() {
            return !TextUtils.isEmpty(getErrorMessage());
        }
    }

    public interface ValidationListener {
        void onValidationStarting(Form form, View formView);

        void onValidateField(Form form, String FieldName, Object value,
                             View viewCause, FormValidator.FieldValidator validator,
                             boolean validationState);

        void onValidationCompleted(Form form, View formView,
                                   FormState state);
    }
}
