package android.istat.base.forms;

import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Pattern;

import android.text.TextUtils;
import android.view.View;

public class FormValidator {
	FormState formState;
	HashMap<String, String> condition = new HashMap<String, String>();

	public final static FormState checkup(Form form, View formView,
			HashMap<String, String> conditions) {
		FormValidator validator = new FormValidator();
		validator.setCondition(conditions);
		return validator.checkForm(form, formView);
	}

	public void setCondition(HashMap<String, String> condition) {
		if (condition == null) {
			return;
		}
		this.condition = condition;
	}

	public FormValidator addCondition(String field, String regex) {
		condition.put(field, regex);
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
			String regexchecker = condition.get(key);
			String value = form.optString(key);
			if (!TextUtils.isEmpty(regexchecker)) {
				if (!Pattern.matches(regexchecker, value)) {
					FormError error = new FormError(key, value, regexchecker);
					error.cause = formView.findViewWithTag(key);
					state.addError(error);
				}
			}
		}
	}
}
