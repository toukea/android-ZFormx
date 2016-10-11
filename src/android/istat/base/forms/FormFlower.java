package android.istat.base.forms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.istat.base.forms.interfaces.FieldHandler;
import android.istat.base.forms.interfaces.FormHandler;
import android.istat.base.forms.tools.FormTools;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * @author istat
 */
public final class FormFlower extends FormGetSetter {

    FormFlower(Form form) {
        super(form);
    }

    public static FormFlower flowIntoView(Form form, View view,
                                          FieldValueSetter... fieldHandlers) {
        return flowIntoView(form, view, false, fieldHandlers);
    }

    public static FormFlower flowIntoView(Form form, View view,
                                          boolean editableOnly, FieldValueSetter... fieldHandlers) {
        return flowIntoView(form, view, editableOnly,
                fieldHandlers != null ? Arrays.asList(fieldHandlers) : null);
    }

    public static FormFlower flowIntoView(Form form, View view,
                                          boolean editableOnly, List<FieldValueSetter> fieldHandlers) {

        FormFlower flower = new FormFlower(form);
        if (fieldHandlers != null && fieldHandlers.size() > 0) {
            List<FieldHandler> handlers = new ArrayList<FieldHandler>();
            handlers.addAll(fieldHandlers);
            flower.addAllFieldHandlers(handlers);
        }
        flower.setModifyEditableOnly(editableOnly);
        flower.mutateView(view);
        return flower;
    }

    public static abstract class FieldValueSetter<T, V extends View> extends FieldValueGetSetter {

        public abstract void setValue(T entity, V v);

        @Override
        public final boolean onHandle(Form form, String fieldName, View view) {
            Class clazzView = getViewTypeClass();
            if (view.getClass().isAssignableFrom(clazzView)) {
                T value = form.opt(fieldName);
                setValue(value, (V) view);
            }
            return super.onHandle(form, fieldName, view);
        }
    }

    public final static class TextViewValueSetter extends FieldValueSetter<String, TextView> {

        @Override
        public void setValue(String entity, TextView textView) {

        }
    }
}
