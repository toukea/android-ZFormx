package android.istat.base.forms;

import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Pattern;

import android.text.TextUtils;

public class FormValidator {
	FormState formState;
	HashMap<String, String> condition = new HashMap<String, String>();

	public final static FormState checkup(Form form,
			HashMap<String, String> condition) {
		FormValidator validator = new FormValidator();
		validator.setCondition(condition);
		return validator.checkForm(form);
	}

	public void setCondition(HashMap<String, String> condition) {
		this.condition = condition;
	}

	public FormValidator addCondition(String field, String regex) {
		condition.put(field, regex);
		return this;
	}

	public FormState checkForm(Form form) {
		FormState state = new FormState();
		proceedCheckup(form, state);
		return state;
	}

	private void proceedCheckup(Form form, FormState state) {
		Iterator<String> iterator = form.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			String regexchecker = condition.get(key);
			String value = form.optString(key);
			if (!TextUtils.isEmpty(regexchecker)) {
				if (!Pattern.matches(regexchecker, value)) {
					FormError error = new FormError(key, value, regexchecker);
					state.addError(error);
				}
			}
		}
	}
}
