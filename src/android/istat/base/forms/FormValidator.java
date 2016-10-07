package android.istat.base.forms;

import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Pattern;

import android.text.TextUtils;
import android.view.View;

public class FormValidator {
    FormState formState;
    HashMap<String, Validator> validationController = new HashMap<String, Validator>();

//    public final static FormState checkup(Form form, View formView,
//                                          HashMap<String, String> conditions) {
//        HashMap<String, Validator> map = new HashMap<String, Validator>();
//        Iterator<String> iterators = conditions.keySet().iterator();
//        while (iterators.hasNext()) {
//            String name = iterators.next();
//            String value = conditions.get(name);
//            map.put(name, new Validator(value));
//
//        }
//        return checkup(form, formView, map);
//    }

    public final static FormState checkup(Form form, View formView,
                                          HashMap<String, Validator> conditions) {
        FormValidator validator = new FormValidator();
        validator.setValidationController(conditions);
        return validator.checkForm(form, formView);
    }

    public void setValidationController(HashMap<String, Validator> validationController) {
        if (validationController == null) {
            return;
        }
        this.validationController = validationController;
    }

    public FormValidator addCondition(String field, Validator validator) {
        validationController.put(field, validator);
        return this;
    }

    public FormState checkForm(Form form, View formView) {
        FormState state = new FormState();
        proceedCheckup(form, state, formView);
        return state;
    }

    private void proceedCheckup(Form form, FormState state, View formView) {
        Iterator<String> iterator = form.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            Validator validator = validationController.get(key);
            String regexChecker = validator.getRegexCondition();
            String value = form.optString(key);
            if (!TextUtils.isEmpty(regexChecker)) {
                if (!Pattern.matches(regexChecker, value)) {
                    FormError error = new FormError(key, value, regexChecker);
                    error.viewCause = formView.findViewWithTag(key);
                    error.setMessage(validator.getMessage());
                    state.addError(error);
                }
            }
        }
    }

    public final static class Validator {
        String regexCondition;
        String message = "";

        public Validator(String condition, String message) {
            this.regexCondition = condition;
            this.message = message;
        }

        public Validator(String condition) {
            this.regexCondition = condition;
        }

        public String getMessage() {
            return message;
        }

        public String getRegexCondition() {
            return regexCondition;
        }
    }
}
