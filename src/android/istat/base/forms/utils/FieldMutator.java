package android.istat.base.forms.utils;

import android.istat.base.forms.Form;
import android.istat.base.forms.interfaces.FieldHandler;
import android.view.View;

/**
 * Created by istat on 11/10/16.
 */
// TODO Make it better
public abstract class FieldMutator<FieldContentType> {
    Form form;

    public FieldMutator(Form form) {
        this.fillFrom(form);
    }

    public final void fillFrom(Form form) {

    }

    public final void flowOn(View v) {

    }

    public final Form getForm() {
        return form;
    }

    public abstract FieldContentType getValue(View v);

    public abstract void setValue(FieldContentType value, View v);

    final FieldHandler getGeter() {
        return new FieldHandler() {
            @Override
            public boolean onHandle(Form form, String fieldName, View view) {
                form.put(fieldName, getValue(view));
                return false;
            }
        };
    }

    final FieldHandler getSeter() {
        return new FieldHandler() {
            @Override
            public boolean onHandle(Form form, String fieldName, View view) {
                Object value = form.get(fieldName);
                try {
                    setValue((FieldContentType) value, view);
                } catch (Exception e) {
                    e.printStackTrace();
                    setValue(null, view);
                }
                return false;
            }
        };
    }
}
