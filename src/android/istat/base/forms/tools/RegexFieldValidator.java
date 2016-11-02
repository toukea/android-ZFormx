package android.istat.base.forms.tools;

import java.util.regex.Pattern;

import org.json.JSONObject;

import android.istat.base.forms.Form;
import android.istat.base.forms.FormValidator.FieldValidator;
import android.text.TextUtils;

public final class RegexFieldValidator extends FieldValidator {
    private String regexCondition;
    private String errorMessage = "";
    boolean breakValidationIfError = false;

    public RegexFieldValidator(boolean breakValidationIfError) {
        super(breakValidationIfError);
    }

    public RegexFieldValidator(String condition, String errorMessage) {
        super();
        this.regexCondition = condition;
        this.errorMessage = errorMessage;
    }

    private RegexFieldValidator() {
        super();
    }

    public void setBreakValidationIfError(boolean breakIfError) {
        this.breakValidationIfError = breakIfError;
    }

    public boolean hasBreakValidationIfErrorEnable() {
        return breakValidationIfError;
    }

    public RegexFieldValidator(String condition) {
        this.regexCondition = condition;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    public final String getRegexCondition() {
        return regexCondition;
    }

    public JSONObject toJson() {
        return null;
    }

    public final static FieldValidator fromJson(JSONObject json) {
        RegexFieldValidator validator = new RegexFieldValidator();
        validator.fillFrom(json);
        return validator;
    }

    @Override
    protected boolean onValidate(Form form, String key, Object objValue) {
        String value = "" + objValue;
        String regexChecker = this.getRegexCondition();
        if (!TextUtils.isEmpty(regexChecker)) {
            if (!Pattern.matches(regexChecker, value)) {
                return false;
            }
        }
        return true;
    }
}