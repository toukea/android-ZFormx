package android.istat.base.forms;

import android.view.View;

public class FormError {
    View viewCause;
    String fieldName;
    String fieldValue;
    String fieldCondition;
    String message;

    FormError(String name, String value, String condition, String message) {
        this.fieldName = name;
        this.fieldCondition = condition;
        this.fieldValue = value;
        this.message = message;
    }

    FormError(String name, String value, String condition) {
        this.fieldName = name;
        this.fieldCondition = condition;
        this.fieldValue = value;
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

    public String getFieldCondition() {
        return fieldCondition;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
