package android.istat.base.forms;

import java.util.ArrayList;
import java.util.List;

public class FormCheckSummary extends ArrayList<FormState> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean hasError() {
		for (FormState state : this) {
			if (state != null && state.hasError()) {
				return true;
			}
		}
		return false;
	}

	public List<FormState> getErrorStates() {
		List<FormState> states = new ArrayList<FormState>();
		for (FormState state : this) {
			if (state.hasError()) {
				states.add(state);
			}
		}
		return states;

	}
}
