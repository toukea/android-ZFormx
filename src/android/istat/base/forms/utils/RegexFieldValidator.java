package android.istat.base.forms.utils;

import java.util.regex.Pattern;

import org.json.JSONObject;

import android.istat.base.forms.Form;
import android.istat.base.forms.FormValidator.FieldValidator;
import android.text.TextUtils;

public final class RegexFieldValidator extends FieldValidator {
	String regexCondition;
	String message = "";
	boolean breakValidationIfError = false;

	public RegexFieldValidator(String condition, String message) {
		this.regexCondition = condition;
		this.message = message;
	}

	private RegexFieldValidator() {
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
		return message;
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