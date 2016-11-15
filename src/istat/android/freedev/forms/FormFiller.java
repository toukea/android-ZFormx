package istat.android.freedev.forms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import istat.android.freedev.forms.tools.FormTools;
import istat.android.freedev.forms.utils.ClassFormLoader;
import istat.android.freedev.forms.utils.ViewUtil;

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
    private final List<FieldViewGetSetter<?, ?>> getters = new ArrayList<FieldViewGetSetter<?, ?>>();

    public static FormFiller use(Form form) {
        return new FormFiller(form);
    }

    FormFiller(Form form) {
        super(form);
    }

    public Form fillWith(View v) {
        handleView(v, getters);
        return form;
    }

    public Form fillWith(Object obj, ClassFormLoader loader) {
        form.fillFrom(obj, loader);
        return form;
    }

    public Form fillWith(Object obj) {
        form.fillFrom(obj);
        return form;
    }

    public FormFiller addViewGetter(FieldViewGetter getter) {
        getters.add(getter);
        return this;
    }

    public FormFiller addViewGetter(FieldViewGetter... getters) {
        for (FieldViewGetter getter : getters) {
            addViewGetter(getter);
        }
        return this;
    }

    public FormFiller addViewGetter(List<FieldViewGetter> getters) {
        for (FieldViewGetter getter : getters) {
            addViewGetter(getter);
        }
        return this;
    }

    public FormFiller setFillEditableOnly(boolean state) {
        setEditableOnlyGetSettable(state);
        return this;
    }

    /**
     * fill a form use view.
     * usign default Getters only.
     *
     * @param form
     * @param view
     */
    public static void fillWithView(Form form, View view) {
        fillWithView(form, view, false, new FieldViewGetter<?, ?>[0]);
    }

    /**
     * fill a form use view.
     *
     * @param form
     * @param view
     * @param getters
     */
    public static void fillWithView(Form form, View view,
                                    FieldViewGetter<?, ?>... getters) {
        fillWithView(form, view, false, getters);
    }

    /**
     * fill a form use view.
     *
     * @param form
     * @param view
     * @param editableOnly specify if only editable field should be flow on.
     * @param getters
     */
    public static void fillWithView(Form form, View view, boolean editableOnly,
                                    FieldViewGetter<?, ?>... getters) {

        fillWithView(form, view, false,
                getters != null ? Arrays.asList(getters) : null);
    }

    /**
     * fill a form use view.
     *
     * @param form
     * @param view
     * @param policy
     */
    public static void fillWithView(Form form, View view, FillerPolicy policy) {
        fillWithView(form, view, policy != null ? policy.editableOnly : false, policy != null ? policy.fieldGetters : null);
    }

    /**
     * fill a form use view.
     *
     * @param form
     * @param view
     * @param editableOnly specify if only editable field should be flow on.
     * @param getters
     */
    public static void fillWithView(Form form, View view, boolean editableOnly,
                                    List<FieldViewGetter<?, ?>> getters) {
        FormFiller filler = new FormFiller(form);
        List<FieldViewGetSetter<?, ?>> handlers = new ArrayList<FieldViewGetSetter<?, ?>>();
        if (getters != null && getters.size() > 0) {
            handlers.addAll(getters);
        }
        filler.setEditableOnlyGetSettable(editableOnly);
        filler.handleView(view, handlers);
    }

    public static abstract class FieldFiller<V extends View> extends
            FieldViewGetter<Object, V> {

    }

    public static abstract class FieldViewGetter<T, V extends View> extends
            FieldViewGetSetter<T, V> {
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

    public final static FieldViewGetter<Integer, Spinner> GETTER_SPINNER_INDEX = new FieldViewGetter<Integer, Spinner>() {
        @Override
        public Integer getValue(Spinner spinner) {
            return spinner.getSelectedItemPosition();
        }
    };
    public final static FieldViewGetter<String, Spinner> GETTER_SPINNER_TEXT = new FieldViewGetter<String, Spinner>() {
        @Override
        public String getValue(Spinner spinner) {
            return FormTools.parseString(spinner.getSelectedItem());
        }
    };
    public final static FieldViewGetter<Object, Spinner> GETTER_SPINNER_ENTITY = new FieldViewGetter<Object, Spinner>() {
        @Override
        public Object getValue(Spinner spinner) {
            return spinner.getSelectedItem();
        }
    };
    public final static FieldViewGetter<Object, RadioGroup> GETTER_RADIO_GROUP_SELECTION_TEXT = new FieldViewGetter<Object, RadioGroup>() {
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
    final static FieldViewGetter<Object, View> DEFAULT_GETTER = new FieldViewGetter<Object, View>() {

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
    protected final List<FieldViewGetSetter<?, ?>> getDefaultHandlers() {
        return new ArrayList<FieldViewGetSetter<?, ?>>() {
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
        final List<FieldViewGetter<?, ?>> fieldGetters = new ArrayList<FieldViewGetter<?, ?>>();

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

        public List<FieldViewGetter<?, ?>> getFielGetters() {
            return fieldGetters;
        }

        public boolean isEditableOnly() {
            return editableOnly;
        }

        public FillerPolicy setEditableOnly(boolean editableOnly) {
            this.editableOnly = editableOnly;
            return this;
        }

        public FillerPolicy setGetters(List<FieldViewGetter<?, ?>> getters) {
            this.fieldGetters.clear();
            this.fieldGetters.addAll(getters);
            return this;
        }

        public FillerPolicy appendGetter(FieldViewGetter<?, ?> getter) {
            this.fieldGetters.add(getter);
            return this;
        }
    }
}
