package istat.android.freedev.forms.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import istat.android.freedev.forms.FormValidator;
import istat.android.freedev.forms.FormValidator.FieldValidator;

public class FormConstraintBuilder {
	protected final HashMap<String, List<FieldValidator>> conditionMap = new HashMap<String, List<FieldValidator>>();

	public FormConstraintBuilder() {

	}

	public FormConstraintBuilder(
			HashMap<String, List<FieldValidator>> conditionMap) {
		this.conditionMap.putAll(conditionMap);
	}

	public FormConstraintBuilder appendFieldValidator(String fieldName,
													  FieldValidator validator) {
		List<FieldValidator> validators = conditionMap.get(fieldName);
		if (validators == null) {
			validators = new ArrayList<FieldValidator>();
		}
		validators.add(validator);
		conditionMap.put(fieldName, validators);
		return this;
	}

	public <T extends FieldValidator> FormConstraintBuilder appendAllFieldValidator(
			String fieldName, List<T> validator) {
		List<FieldValidator> validators = conditionMap.get(fieldName);
		if (validators == null) {
			validators = new ArrayList<FieldValidator>();
		}
		validators.addAll(validator);
		conditionMap.put(fieldName, validators);
		return this;
	}

	public FormConstraintBuilder applyFieldValidators(String fieldName,
													  List<FieldValidator> validators) {
		conditionMap.put(fieldName, validators);
		return this;
	}

	public FormConstraintBuilder applyFieldValidator(String fieldName,
													 FieldValidator validator) {
		List<FieldValidator> validators = new ArrayList<FormValidator.FieldValidator>();
		validators.add(validator);
		conditionMap.put(fieldName, validators);
		return this;
	}

	public List<FieldValidator> getFieldValidators(String fieldName) {
		return conditionMap.get(fieldName);
	}

	public final HashMap<String, List<FieldValidator>> create() {
		return conditionMap;
	}

	public JSONObject toJson() {
		return null;

	}
}