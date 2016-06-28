package android.istat.base.forms;

import java.util.ArrayList;
import java.util.List;

public class FormState {
	List<FormError> errors = new ArrayList<FormError>();

	public List<FormError> getErrors() {
		return errors;
	}

	public boolean hasError() {
		return errors != null && !errors.isEmpty();
	}

	public FormState addError(FormError error) {
		errors.add(error);
		return this;
	}
}
