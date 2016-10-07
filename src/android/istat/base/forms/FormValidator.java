package android.istat.base.forms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import android.text.TextUtils;
import android.view.View;

public final class FormValidator {
    FormState formState;
    HashMap<String, List<Validator>> validationCondition = new HashMap<String, List<Validator>>();

    public final static FormState checkup(Form form, View formView,
                                          FormValidationCondition condition) {
        return checkup(form, formView, condition.getConditionMap());
    }

    public final static FormState checkup(Form form, View formView,
                                          HashMap<String, List<Validator>> conditions) {
        FormValidator validator = new FormValidator();
        validator.setValidationCondition(conditions);
        return validator.checkForm(form, formView);
    }

    public void setValidationCondition(FormValidationCondition condition) {
        setValidationCondition(condition.getConditionMap());
    }

    public void setValidationCondition(HashMap<String, List<Validator>> validationController) {
        if (validationController == null) {
            return;
        }
        this.validationCondition = validationController;
    }

    public FormValidator addCondition(String field, Validator validator) {
        List<Validator> validators = validationCondition.get(field);
        if (validators == null) {
            validators = new ArrayList<Validator>();
        }
        validators.add(validator);
        validationCondition.put(field, validators);
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
            List<Validator> validators = validationCondition.get(key);
            for (Validator validator : validators) {
                String regexChecker = validator.getRegexCondition();
                String value = form.optString(key);
                if (!TextUtils.isEmpty(regexChecker)) {
                    if (!Pattern.matches(regexChecker, value)) {
                        FormError error = new FormError(key, value, regexChecker);
                        error.viewCause = formView.findViewWithTag(key);
                        error.addFailedValidators(validator);
                        state.addError(error);
                        if (validator.isBreakIfError()) {
                            break;
                        }
                    }
                }
            }
        }
    }

    public final static class FormValidationCondition {
        final HashMap<String, List<Validator>> conditionMap = new HashMap<String, List<Validator>>();

        public FormValidationCondition addFieldValidator(String fieldName, Validator validator) {
            List<Validator> validators = conditionMap.get(fieldName);
            if (validators == null) {
                validators = new ArrayList<Validator>();
            }
            validators.add(validator);
            conditionMap.put(fieldName, validators);
            return this;
        }

        public FormValidationCondition setFieldValidators(String fieldName, List<Validator> validators) {
            conditionMap.put(fieldName, validators);
            return this;
        }

        public List<Validator> getFieldValidators(String fieldName) {
            return conditionMap.get(fieldName);
        }

        HashMap<String, List<Validator>> getConditionMap() {
            return conditionMap;
        }
    }

    public final static class Validator {
        String regexCondition;
        String message = "";
        boolean breakIfError = false;

        public Validator(String condition, String message) {
            this.regexCondition = condition;
            this.message = message;
        }

        public void setBreakIfError(boolean breakIfError) {
            this.breakIfError = breakIfError;
        }

        public boolean isBreakIfError() {
            return breakIfError;
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
