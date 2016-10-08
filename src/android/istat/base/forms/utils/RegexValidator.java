package android.istat.base.forms.utils;

import java.util.regex.Pattern;

import org.json.JSONObject;

import android.istat.base.forms.Form;
import android.istat.base.forms.FormValidator.Validator;
import android.text.TextUtils;

public class RegexValidator extends Validator {
	String regexCondition;
	String message = "";
	boolean breakValidationIfError = false;

	public RegexValidator(String condition, String message) {
		this.regexCondition = condition;
		this.message = message;
	}

	public void setBreakValidationIfError(boolean breakIfError) {
		this.breakValidationIfError = breakIfError;
	}

	public boolean hasBreakValidationIfErrorEnable() {
		return breakValidationIfError;
	}

	public RegexValidator(String condition) {
		this.regexCondition = condition;
	}

	public String getMessage() {
		return message;
	}

	public String getRegexCondition() {
		return regexCondition;
	}

	public JSONObject toJson() {
		return null;
	}

	public final static Validator fromJson(JSONObject json) {
		return null;
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