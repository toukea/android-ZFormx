package istat.android.freedev.forms.tools;

import android.view.View;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Iterator;

import istat.android.freedev.forms.Form;
import istat.android.freedev.forms.FormFiller;
import istat.android.freedev.forms.FormState;
import istat.android.freedev.forms.FormValidator;

/**
 * Created by istat on 22/11/16.
 */

public final class LazyFormValidator extends RegexFormValidatorBuilder {
    FormValidator.ValidationListener validationListener;

    FormValidator.ValidationListener mValidationListener = new FormValidator.ValidationListener() {
        @Override
        public void onValidationStarting(Form form, View formView) {
            if (validationListener != null) {
                validationListener.onValidationStarting(form, formView);
            }
        }

        @Override
        public void onValidateField(Form form, String FieldName, Object value, View viewCause, FormValidator.FieldValidator validator, boolean validationState) {
            if (viewCause != null) {
                Class viewClass = viewCause.getClass();
                ErrorAdapter adapter = errorAdapters.get(viewClass);
                if (adapter == null) {
                    adapter = getMostAssignable(viewCause);
                }
                if (adapter != null) {
                    adapter.onNotifyError(viewCause, validator);
                }
            }

            if (validationListener != null) {
                validationListener.onValidateField(form, FieldName, value, viewCause, validator, validationState);
            }
        }

        @Override
        public void onValidationCompleted(Form form, View formView, FormState state) {
            if (validationListener != null) {
                validationListener.onValidationCompleted(form, formView, state);
            }
        }
    };

    public void setValidationListener(FormValidator.ValidationListener validationListener) {
        this.validationListener = validationListener;
    }


    private ErrorAdapter getMostAssignable(View viewCause) {
        Iterator<Class<?>> iterator = errorAdapters.keySet().iterator();
        while (iterator.hasNext()) {
            Class<?> cLass = iterator.next();
            if (cLass.isAssignableFrom(viewCause.getClass()) || viewCause.getClass().isAssignableFrom(cLass)) {
                return errorAdapters.get(cLass);
            }
        }
        return null;
    }


    final HashMap<Class<?>, ErrorAdapter> errorAdapters = new HashMap<Class<?>, ErrorAdapter>() {
        {
            put(TextView.class, TEXT_VIEW_ERROR_ADAPTER);
        }
    };

    public final static ErrorAdapter<TextView> TEXT_VIEW_ERROR_ADAPTER = new ErrorAdapter<TextView>() {
        @Override
        public void onNotifyError(TextView textView, FormValidator.FieldValidator validator) {
            if (validator.hasErrorMessage()) {
                textView.setError(validator.getErrorMessage());
            }
        }
    };

    public <V extends View> void putErrorAdapter(Class<V> viewClass, ErrorAdapter<V> adapter) {
        if (adapter != null) {
            errorAdapters.put(viewClass, adapter);
        }
    }

    public static interface ErrorAdapter<V extends View> {
        public abstract void onNotifyError(V v, FormValidator.FieldValidator validator);
    }

    public LazyFormValidator() {

    }

    FormFiller.FillerPolicy fillerPolicy;

    public void setFillerPolicy(FormFiller.FillerPolicy fillerPolicy) {
        this.fillerPolicy = fillerPolicy;
    }

    public FormValidator validate(Form form) {
        FormValidator validator = create();
        validator.validate(form);
        return validator;
    }

    public FormValidator validate(Form form, View view) {
        FormValidator validator = create();
        validator.validate(form, view);
        return validator;
    }

    public FormValidator create() {
        FormValidator validator = new FormValidator();
        validator.setFillerPolicy(fillerPolicy);
        validator.setConstraints(conditionBuilder.create());
        validator.setValidationListener(mValidationListener);
        return validator;
    }


    public static abstract class LazyValidationListener implements FormValidator.ValidationListener {
        @Override
        public void onValidateField(Form form, String FieldName, Object value, View viewCause, FormValidator.FieldValidator validator, boolean validationState) {

        }
    }
}
