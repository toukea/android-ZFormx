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

    public final static String REGEX_PATTERN_URL = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
    public final static String REGEX_PATTERN_EMAIL = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
    public final static String REGEX_PATTERN_PHONE = "^(\\+)?[0-9]{6,}$";
    public final static String REGEX_PATTERN_NUMBER = "\\d";
    public final static String REGEX_PATTERN_NAME = ".+";
    public final static String REGEX_PATTERN_FILE_PATH = "^.+[^/]$";
    public final static String REGEX_PATTERN_DIRECTORY_PATH = "^.*/$";
}