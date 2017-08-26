package istat.android.freedev.forms.tools;

import android.text.TextUtils;
import android.view.View;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import istat.android.freedev.forms.Form;
import istat.android.freedev.forms.FormFieldError;
import istat.android.freedev.forms.FormFiller;
import istat.android.freedev.forms.FormState;
import istat.android.freedev.forms.FormValidator;

/**
 * @author istat
 */
public class FormValidatorHelper {
    private FormValidatorHelper() {

    }

    public final static FormState validate(Form form,
                                           HashMap<String, List<istat.android.freedev.forms.FormValidator.FieldValidator>> constraints) {
        return validate(form, constraints, null);
    }

    public final static FormState validate(Form form,
                                           HashMap<String, List<istat.android.freedev.forms.FormValidator.FieldValidator>> constraints, FormValidator.ValidationListener listener) {
        return validate(form, null, constraints, listener);
    }

    public final static FormState validate(View formView,
                                           HashMap<String, List<istat.android.freedev.forms.FormValidator.FieldValidator>> constraints) {
        FormFiller.FillerPolicy nullFillerPolicy = null;
        return validate(formView, nullFillerPolicy, constraints, null);
    }

    public final static FormState validate(View formView, FormFiller.FillerPolicy fillerPolicy,
                                           HashMap<String, List<istat.android.freedev.forms.FormValidator.FieldValidator>> constraints, istat.android.freedev.forms.FormValidator.ValidationListener listener) {
        FormValidator validator = new FormValidator();
        validator.setConstraints(constraints);
        validator.setValidationListener(listener);
        return validator.validate(formView, fillerPolicy);
    }

    public final static FormState validate(Form form, View formView,
                                           HashMap<String, List<istat.android.freedev.forms.FormValidator.FieldValidator>> constraints) {
        FormValidator validator = new FormValidator();
        validator.setConstraints(constraints);
        return validator.validate(form, formView);
    }

    public final static FormState validate(Form form, View formView,
                                           HashMap<String, List<FormValidator.FieldValidator>> constraints, FormValidator.ValidationListener listener) {
        FormValidator validator = new FormValidator();
        validator.setConstraints(constraints);
        validator.setValidationListener(listener);
        return validator.validate(form, formView);
    }
}
