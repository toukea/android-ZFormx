package istat.android.freedev.forms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import istat.android.freedev.forms.tools.FormTools;
import istat.android.freedev.forms.utils.ViewUtil;

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

    public void flowOn(View v) {
        handleView(v);
    }

    public <T> T flowOn(T obj) {
        form.flowOn(obj);
        return obj;
    }

    public FormFlower setFlowEditableonly(boolean state) {
        setEditableOnlyGetSettable(state);
        return this;
    }

    public FormFlower addFieldSetter(FieldFlower flower) {

        return this;
    }
    public FormFlower addFieldSetter(FieldValueSetter setter) {

        return this;
    }

    public static FormFlower from(Form form) {
        return new FormFlower(form);
    }

    /**
     * flow Form contain inside a view using a specific array of setters: {@link FieldFlower} or {@link FieldValueSetter}
     *
     * @param form
     * @param view
     * @param setters
     */
    public static void flowIntoView(Form form, View view,
                                    FieldValueSetter<?, ?>... setters) {
        flowIntoView(form, view, false, setters);
    }

    /**
     * * flow Form contain inside a view using a specific array of setters: {@link FieldFlower} or {@link FieldValueSetter}
     *
     * @param form
     * @param view
     * @param editableOnly specify if only editable field should be flow on.
     * @param setters
     */
    public static void flowIntoView(Form form, View view, boolean editableOnly,
                                    FieldValueSetter<?, ?>... setters) {
        flowIntoView(form, view, editableOnly,
                setters != null ? Arrays.asList(setters) : null);
    }

    /**
     * flow Form contain inside a view using a specific array of setters: {@link FieldFlower} or {@link FieldValueSetter}
     *
     * @param form
     * @param view
     * @param editableOnly
     * @param setters
     */
    public static void flowIntoView(Form form, View view, boolean editableOnly,
                                    List<FieldValueSetter<?, ?>> setters) {
        FormFlower flower = new FormFlower(form);
        List<FieldValueGetSetter<?, ?>> handlers = new ArrayList<FieldValueGetSetter<?, ?>>();
        if (setters != null && setters.size() > 0) {
            handlers.addAll(setters);
        }
        flower.setEditableOnlyGetSettable(editableOnly);
        flower.handleView(view, handlers);

    }

    /**
     * @param <T>
     * @param <V>
     */
    public static abstract class FieldValueSetter<T, V extends View> extends
            FieldValueGetSetter<T, V> {

        public abstract void setValue(T entity, V v);

        @SuppressWarnings("unchecked")
        @Override
        public final boolean onHandle(Form form, String fieldName,
                                      final View view) {
            if (isHandleAble(view)) {
                final T value = form.opt(fieldName);
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        setValue(value, (V) view);
                    }
                });
                return true;
            }
            return false;
        }
    }

    final FieldValueSetter<Integer, Spinner> SETTER_SPINNER_INDEX = new FieldValueSetter<Integer, Spinner>() {

        @Override
        public void setValue(Integer entity, Spinner v) {
            v.setSelection(FormTools.parseInt(entity));
        }
    };
    final FieldValueSetter<String, Spinner> SETTER_SPINNER_CONTAINT = new FieldValueSetter<String, Spinner>() {

        @Override
        public void setValue(String entity, Spinner spinner) {

        }
    };

    public static abstract class FieldFlower<V extends View> extends
            FieldValueSetter<Object, V> {

    }

    final FieldFlower<TextView> SETTER_TEXT_VIEW_TEXT = new FieldFlower<TextView>() {

        @Override
        public void setValue(Object value, TextView v) {
            v.setText(FormTools.parseString(value));
        }
    };
    public final static FieldFlower<CheckBox> SETTER_CHECKBOX_STATE = new FieldFlower<CheckBox>() {

        @Override
        public void setValue(Object value, CheckBox v) {

            v.setChecked(FormTools.parseBoolean(value));

        }
    };
    public final static FieldFlower<RadioButton> SETTER_RADIO_BUTTON_STATE = new FieldFlower<RadioButton>() {

        @Override
        public void setValue(Object value, RadioButton v) {
            v.setChecked(FormTools.parseBoolean(value));
        }
    };

    public final static FieldFlower<RadioGroup> SETTER_RADIO_GROUP_SELECTED_RADIATION_INDEX = new FieldFlower<RadioGroup>() {

        @Override
        public void setValue(Object value, RadioGroup v) {
            int selectionIndex = FormTools.parseInt(value);
            List<View> viewChild = ViewUtil.getAllChildViews(v);
            int index = 0;
            for (View child : viewChild) {
                if (child instanceof RadioButton) {
                    if (index == selectionIndex) {
                        ((RadioButton) child).setSelected(true);
                        break;
                    }
                    index++;
                }
            }
        }
    };

    @Override
    protected final List<FieldValueGetSetter<?, ?>> getDefaultHandlers() {
        return new ArrayList<FieldValueGetSetter<?, ?>>() {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            {
                add(SETTER_SPINNER_INDEX);
                add(SETTER_TEXT_VIEW_TEXT);
                add(SETTER_CHECKBOX_STATE);
                add(SETTER_RADIO_BUTTON_STATE);
                add(SETTER_RADIO_GROUP_SELECTED_RADIATION_INDEX);
            }
        };
    }

    protected final void handleViewGroup(ViewGroup v) {
        String[] fields = form.getFieldNames();
        for (String field : fields) {
            View view = v.findViewWithTag(field);
            onHandleView(view);
        }
    }
}
