package istat.android.freedev.forms;

import android.text.TextUtils;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public final class FormFieldError {
    View viewCause;
    String fieldName;
    Object fieldValue;
    final List<FormValidator.FieldValidator> failedValidators = new ArrayList<FormValidator.FieldValidator>();

    FormFieldError(String name, Object value) {
        this.fieldName = name;
        this.fieldValue = value;
    }

    FormFieldError(String name, Object value,
                   List<FormValidator.FieldValidator> failedValidators) {
        this.fieldName = name;
        this.fieldValue = value;
        this.failedValidators.addAll(failedValidators);
    }

    FormFieldError() {
    }

    public List<String> getMessages() {
        List<String> messages = new ArrayList<String>();
        for (FormValidator.FieldValidator validator : failedValidators) {
            messages.add(validator.getErrorMessage());
        }
        return messages;
    }

    public String getFirstMessage() {
        for (FormValidator.FieldValidator validator : failedValidators) {
            String message = validator.getErrorMessage();
            if (!TextUtils.isEmpty(message)) {
                return message;
            }
        }
        return "";
    }

    public String getMessage(int index) {
        int count = 0;
        for (FormValidator.FieldValidator validator : failedValidators) {
            String message = validator.getErrorMessage();
            if (!TextUtils.isEmpty(message)) {
                if (count == index) {
                    return message;
                }
                count++;
            }
        }
        return "";
    }

    public String getMessageAtIndexs(int index) {
        if (index < failedValidators.size()) {
            return failedValidators.get(index).getErrorMessage();
        }
        return null;
    }

    public boolean hasViewCause() {
        return viewCause != null;
    }

    public Object getFieldValue() {
        return fieldValue;
    }

    public View getViewCause() {
        return viewCause;
    }

    public List<FormValidator.FieldValidator> getFailedValidators() {
        return failedValidators;
    }

    public String getFieldName() {
        return fieldName;
    }

    void addFailedValidators(FormValidator.FieldValidator validator) {
        failedValidators.add(validator);
    }

    public static class ViewNotSupportedException extends RuntimeException {
        public ViewNotSupportedException(String message) {
            super(message);
        }

        public ViewNotSupportedException(Throwable cause) {
            super(cause);
        }
    }
}
