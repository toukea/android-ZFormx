package android.istat.base.forms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.istat.base.forms.tools.FormTools;
import android.istat.base.forms.utils.ViewUtil;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * @author istat
 */
public final class FormFiller extends FormGetSetter {

    FormFiller(Form form) {
        super(form);
    }

    public static void fillFromView(Form form, View view, boolean editableOnly,
                                    FieldValueGetter<?, ?>... fieldHandlers) {

        fillFromView(form, view, false,
                fieldHandlers != null ? Arrays.asList(fieldHandlers) : null);
    }

    public static void fillFromView(Form form, View view,
                                    FieldValueGetter<?, ?>... fieldHandlers) {
        fillFromView(form, view, false, fieldHandlers);
    }

    public static void fillFromView(Form form, View view, FillerPolicy policy) {
        fillFromView(form, view, policy != null ? policy.editableOnly : false, policy != null ? policy.fieldGetters : null);
    }

    public static void fillFromView(Form form, View view, boolean editableOnly,
                                    List<FieldValueGetter<?, ?>> fieldHandlers) {
        FormFiller filler = new FormFiller(form);
        List<FieldValueGetter<?, ?>> handlers = new ArrayList<FieldValueGetter<?, ?>>();
        if (fieldHandlers != null && fieldHandlers.size() > 0) {
            handlers.addAll(fieldHandlers);
        }
        filler.setEditableOnlyGetSettable(editableOnly);
        filler.handleView(view);
    }

    public static abstract class FieldFiller<V extends View> extends
            FieldValueGetter<Object, V> {

    }

    public static abstract class FieldValueGetter<T, V extends View> extends
            FieldValueGetSetter {
        public abstract T getValue(V v);

        @SuppressWarnings("unchecked")
        @Override
        public final boolean onHandle(Form form, String fieldName, View view) {
            if (isHandleAble(view)) {
                T value = getValue((V) view);
                Log.d("onHandle", "view_Value=" + value);
                if (value != null) {
                    form.put(fieldName, value);
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        }
    }

    public final static FieldValueGetter<Integer, Spinner> GETTER_SPINNER_INDEX = new FieldValueGetter<Integer, Spinner>() {
        @Override
        public Integer getValue(Spinner spinner) {
            return spinner.getSelectedItemPosition();
        }
    };
    public final static FieldValueGetter<String, Spinner> GETTER_SPINNER_TEXT = new FieldValueGetter<String, Spinner>() {
        @Override
        public String getValue(Spinner spinner) {
            return FormTools.parseString(spinner.getSelectedItem());
        }
    };
    public final static FieldValueGetter<Object, Spinner> GETTER_SPINNER_ENTITY = new FieldValueGetter<Object, Spinner>() {
        @Override
        public Object getValue(Spinner spinner) {
            return spinner.getSelectedItem();
        }
    };
    public final static FieldValueGetter<Object, RadioGroup> GETTER_RADIO_GROUP_SELECTION_TEXT = new FieldValueGetter<Object, RadioGroup>() {
        @Override
        public Object getValue(RadioGroup v) {
            int selectionId = v.getCheckedRadioButtonId();
            View selectedView = v.findViewById(selectionId);
            if (selectedView instanceof RadioButton) {
                return ((RadioButton) selectedView).getText();
            }
            return null;
        }
    };
    final static FieldValueGetter<Object, View> DEFAULT_GETTER = new FieldValueGetter<Object, View>() {

        @Override
        public Object getValue(View v) {
            Log.d("onHandle", "view_Type=" + v);
            try {
                if (v instanceof TextView) {
                    TextView t = (TextView) v;
                    return t.getText().toString();
                } else if (v instanceof EditText) {
                    EditText t = (EditText) v;
                    return t.getText().toString();
                } else if (v instanceof CheckBox) {
                    CheckBox t = (CheckBox) v;
                    return t.isChecked();
                } else if (v instanceof Spinner) {
                    Spinner t = (Spinner) v;
                    return t.getSelectedItemPosition();
                } else if (v instanceof RadioButton) {
                    RadioButton t = (RadioButton) v;
                    return t.isChecked();
                } else if (v instanceof RadioGroup) {
                    return GETTER_RADIO_GROUP_SELECTION_TEXT
                            .getValue((RadioGroup) v);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
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
                add(DEFAULT_GETTER);
            }
        };
    }

    @Override
    protected final void handleViewGroup(ViewGroup v) {
        List<View> childV = !isEditableOnlyGetSettable() ? ViewUtil
                .getDirectChildViews(v) : v.getTouchables();
        for (View view : childV) {
            onHandleView(view);
        }
    }

    public final static class FillerPolicy {
        boolean editableOnly;
        final List<FieldValueGetter<?, ?>> fieldGetters = new ArrayList<FieldValueGetter<?, ?>>();

        private FillerPolicy() {

        }

        public final static FillerPolicy newInstance() {
            return new FillerPolicy();
        }

        public final static FillerPolicy newInstance(boolean editableOnly) {
            FillerPolicy policy = new FillerPolicy();
            policy.editableOnly = editableOnly;
            return policy;
        }

        public List<FieldValueGetter<?, ?>> getFielGetters() {
            return fieldGetters;
        }

        public boolean isEditableOnly() {
            return editableOnly;
        }

        public FillerPolicy setEditableOnly(boolean editableOnly) {
            this.editableOnly = editableOnly;
            return this;
        }

        public FillerPolicy setFieldGetters(List<FieldValueGetter<?, ?>> fieldHandlers) {
            this.fieldGetters.clear();
            this.fieldGetters.addAll(fieldHandlers);
            return this;
        }

        public FillerPolicy appendGetter(FieldValueGetter<?, ?> getter) {
            this.fieldGetters.add(getter);
            return this;
        }
    }
}
