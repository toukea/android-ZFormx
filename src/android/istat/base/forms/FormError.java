package android.istat.base.forms;

import android.view.View;

public class FormError {
	String name;
	View cause;
	String value;
	String condition;

	public FormError(String name, String value, String condition) {
		this.name = name;
		this.condition = condition;
	}

	public FormError() {
	}

	public boolean hasCause() {
		return cause != null;
	}

	public String getValue() {
		return value;
	}

	public View getCause() {
		return cause;
	}

	public String getCondition() {
		return condition;
	}

	public String getName() {
		return name;
	}
}
