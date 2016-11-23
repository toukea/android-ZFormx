package istat.android.freedev.forms.tools;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

import istat.android.freedev.forms.Form;
import istat.android.freedev.forms.FormValidator;
import istat.android.freedev.forms.FormValidator.FieldValidator;

public final class RegexFormValidatorBuilder extends RegexFormAbsValidatorBuilder {

    public RegexFormValidatorBuilder() {

    }

    public FormValidator create(Form form) {
        FormValidator validator = FormValidator.from(form);
        validator.setConstraints(conditionBuilder.create());
        return validator;
    }

    public FormValidator create(Form form, View view) {
        FormValidator validator = FormValidator.from(form, view);
        validator.setConstraints(conditionBuilder.create());
        return validator;
    }
}
