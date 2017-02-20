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
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * @author istat
 */
public final class FormFiller extends FormViewHandler {
    private final List<FieldViewHandler<?, ?>> getters = new ArrayList<FieldViewHandler<?, ?>>();

    public static FormFiller use(Form form) {
        return new FormFiller(form);
    }

    public static FormFiller useNewForm() {
        return new FormFiller(new Form());
    }

    public static FormFiller useNewForm(Class<?> model) {
        return use(new Form(), model);
    }

    public static FormFiller use(Form form, Class<?> model) {
        Form formTmp = Form.fromClass(model);
        form.putAll(formTmp);
        return new FormFiller(form);
    }

    public static FormFiller useModel(Class<?> model) {
        Form form = Form.fromClass(model);
        return new FormFiller(form);
    }

    FormFiller(Form form) {
        super(form);
    }

    public FormFiller addFieldToFill(String... fields) {
        form.addFieldNames(fields);
        return this;
    }

    public FormFiller addFieldToFill(List<String> fields) {
        form.addFieldNames(fields.toArray(new String[fields.size()]));
        return this;
    }

    public FormFiller setFieldToFill(String... fields) {
        form.setFieldNames(fields);
        return this;
    }

    public FormFiller setFieldToFill(List<String> fields) {
        form.setFieldNames(fields.toArray(new String[fields.size()]));
        return this;
    }

    public FormFiller addFieldToFill(String field) {
        form.addFieldNames(field);
        return this;
    }

    public Form fillWith(View v) throws FormFieldError.ViewNotSupportedException {
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

    public FormFiller addViewExtractor(ViewValueExtractor getter) {
        getters.add(getter);
        return this;
    }

    public FormFiller throwViewNotSupported(boolean throx) {
        this.throwOnHandlingFail = throx;
        return this;
    }

    public FormFiller addViewExtractor(ViewValueExtractor... getters) {
        for (ViewValueExtractor getter : getters) {
            addViewExtractor(getter);
        }
        return this;
    }

    public FormFiller addViewExtractor(List<ViewValueExtractor> getters) {
        for (ViewValueExtractor getter : getters) {
            addViewExtractor(getter);
        }
        return this;
    }

    public FormFiller setFillAccessibleOnly(boolean state) {
        setAccessibleOnlyGetSettable(state);
        return this;
    }

    /**
     * fill a form use view.
     * usign default Getters only.
     *
     * @param form
     * @param view
     */
    public static void fillWithView(Form form, View view) throws FormFieldError.ViewNotSupportedException {
        fillWithView(form, view, false, new ViewValueExtractor<?, ?>[0]);
    }

    /**
     * fill a form use view.
     *
     * @param form
     * @param view
     * @param getters
     */
    public static void fillWithView(Form form, View view,
                                    ViewValueExtractor<?, ?>... getters) throws FormFieldError.ViewNotSupportedException {
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
                                    ViewValueExtractor<?, ?>... getters) {

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
    public static void fillWithView(Form form, View view, FillerPolicy policy) throws FormFieldError.ViewNotSupportedException {
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
                                    List<ViewValueExtractor<?, ?>> getters) throws FormFieldError.ViewNotSupportedException {
        FormFiller filler = new FormFiller(form);
        List<FieldViewHandler<?, ?>> handlers = new ArrayList<FieldViewHandler<?, ?>>();
        if (getters != null && getters.size() > 0) {
            handlers.addAll(getters);
        }
        filler.setAccessibleOnlyGetSettable(editableOnly);
        filler.handleView(view, handlers);
    }

    public static abstract class FieldFiller<V extends View> extends
            ViewValueExtractor<Object, V> {

        public FieldFiller(Class<V> viewType) {
            super(Object.class, viewType);
        }
    }

    public static abstract class ViewValueExtractor<T, V extends View> extends
            FieldViewHandler<T, V> {
        public ViewValueExtractor(Class<T> valueType, Class<V> viewType) {
            super(valueType, viewType);
        }

        public ViewValueExtractor(Class<T> valueType, Class<V> viewType, String... acceptedField) {
            super(valueType, viewType, acceptedField);
        }

        @Deprecated
        ViewValueExtractor() {
            super();
        }

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

    public final static ViewValueExtractor<Integer, Spinner> GETTER_ADAPTER_VIEW_INDEX = new ViewValueExtractor<Integer, Spinner>(Integer.class, Spinner.class) {
        @Override
        public Integer getValue(Spinner spinner) {
            return spinner.getSelectedItemPosition();
        }
    };
    public final static ViewValueExtractor<String, Spinner> GETTER_ADAPTER_VIEW_TEXT = new ViewValueExtractor<String, Spinner>(String.class, Spinner.class) {
        @Override
        public String getValue(Spinner spinner) {
            return FormTools.parseString(spinner.getSelectedItem());
        }
    };
    public final static FieldFiller<Spinner> GETTER_ADAPTER_VIEW_ENTITY = new FieldFiller<Spinner>(Spinner.class) {
        @Override
        public Object getValue(Spinner spinner) {
            return spinner.getSelectedItem();
        }
    };
    public final static FieldFiller<RadioGroup> GETTER_RADIO_GROUP_SELECTION_TEXT = new FieldFiller<RadioGroup>(RadioGroup.class) {
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
    final static FieldFiller<View> DEFAULT_GETTER = new FieldFiller<View>(View.class) {

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
                } else if (v instanceof AdapterView) {
                    AdapterView t = (AdapterView) v;
                    return t.getSelectedItemPosition();
                } else if (v instanceof ImageView) {
                    ImageView t = (ImageView) v;
                    return t.getContentDescription();
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
    protected final List<FieldViewHandler<?, ?>> getDefaultHandlers() {
        return new ArrayList<FieldViewHandler<?, ?>>() {
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
        String[] fields = form.getFieldNames();
        if (fields != null && fields.length > 0) {
            for (String field : fields) {
                View view = v.findViewWithTag(field);
                onHandleView(view);
            }
        } else {
            if (v.getTag() != null && !"".equals(v.getTag() + "")) {
                onHandleView(v);
            } else {
                List<View> childV = !isAccessibleOnlyGetSettable() ? ViewUtil
                        .getDirectChildViews(v) : v.getTouchables();
                for (View view : childV) {
                    onHandleView(view);
                }
            }
        }
    }

    public final static class FillerPolicy {
        boolean editableOnly;
        final List<ViewValueExtractor<?, ?>> fieldGetters = new ArrayList<ViewValueExtractor<?, ?>>();

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

        public List<ViewValueExtractor<?, ?>> getFieldExtractors() {
            return fieldGetters;
        }

        public boolean isEditableOnly() {
            return editableOnly;
        }

        public FillerPolicy setEditableOnly(boolean editableOnly) {
            this.editableOnly = editableOnly;
            return this;
        }

        public FillerPolicy setExtractor(List<ViewValueExtractor<?, ?>> getters) {
            this.fieldGetters.clear();
            this.fieldGetters.addAll(getters);
            return this;
        }

        public FillerPolicy appendExtractor(ViewValueExtractor<?, ?> getter) {
            this.fieldGetters.add(getter);
            return this;
        }

        public FillerPolicy appendExtractor(FieldFiller<?> getter) {
            this.fieldGetters.add(getter);
            return this;
        }
    }
}
