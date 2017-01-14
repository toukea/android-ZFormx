package istat.android.freedev.forms.tools;

import java.util.regex.Pattern;

import org.json.JSONObject;

import istat.android.freedev.forms.Form;
import istat.android.freedev.forms.FormValidator.FieldValidator;

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
        JSONObject json = new JSONObject();
        return json;
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

    public final static String REGEX_EMAIL = "[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}";
    public final static String REGEX_PHONE = "^(\\+)?[0-9]{6,}$";
    public final static String REGEX_NUMBER = "\\d";
    public final static String REGEX_NAME = ".+";
    public final static String REGEX_FILE_PATH = "^.+[^/]$";
    public final static String REGEX_DIRECTORY_PATH = "^.*/$";
}