package android.istat.base.forms;

import android.text.TextUtils;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class FormState {
    List<FormError> errors = new ArrayList<FormError>();

    public List<FormError> getErrors() {
        return errors;
    }

    public boolean hasError() {
        return errors != null && !errors.isEmpty();
    }

    FormState addError(FormError error) {
        errors.add(error);
        return this;
    }

    public List<FormError> getErrorByViewCause(View cause) {
        List<FormError> errors = new ArrayList<FormError>();
        for (FormError error : getErrors()) {
            if (cause != null && cause.equals(error.getViewCause())) {
                errors.add(error);
            }
        }
        return errors;
    }

    public List<FormError> getErrorByFieldName(String fieldName) {
        List<FormError> errors = new ArrayList<FormError>();
        for (FormError error : getErrors()) {
            if (!TextUtils.isEmpty(fieldName) && fieldName.equals(error.getFieldValue())) {
                errors.add(error);
            }
        }
        return errors;
    }
}
