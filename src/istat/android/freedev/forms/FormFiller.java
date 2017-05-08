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
import android.widget.CompoundButton;
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
    private final List<FieldViewHandler<?, ?>> extractors = new ArrayList<FieldViewHandler<?, ?>>();

    public static FormFiller using(Form form) {
        FormFiller filler = new FormFiller();
        filler.use(form);
        return filler;
    }

    public static FormFiller usingNewForm() {
        FormFiller filler = new FormFiller();
        filler.useNewForm();
        return filler;
    }

    public static FormFiller using(Form form, Class<?> model) {
        FormFiller filler = new FormFiller();
        filler.use(form, model);
        return filler;
    }

    public static FormFiller usingModel(Class<?> model) {
        FormFiller filler = new FormFiller();
        filler.useModel(model);
        return filler;
    }

    public FormFiller use(Form form) {
        this.form = form;
        return this;
    }

    public FormFiller useNewForm() {
        return FormFiller.using(new Form());
    }

    public FormFiller use(Form form, Class<?> model) {
        Form formTmp = Form.fromClass(model);
        form.putAll(formTmp);
        return FormFiller.using(form);
    }

    public FormFiller useModel(Class<?> model) {
        Form form = Form.fromClass(model);
        return FormFiller.using(form);
    }

    public FormFiller() {

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
        handleView(v, extractors);
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

    public void fillWith(Form form, View v) throws FormFieldError.ViewNotSupportedException {
        handleView(v, extractors);
        form.putAll(this.form);
    }

    public void fillWith(Form form, Object obj, ClassFormLoader loader) {
        form.fillFrom(obj, loader);
        form.putAll(this.form);
    }

    public void fillWith(Form form, Object obj) {
        form.fillFrom(obj);
        form.putAll(this.form);
    }

    public FormFiller addViewExtractor(ViewValueExtractor getter) {
        extractors.add(getter);
        return this;
    }

    public FormFiller throwViewNotSupported(boolean throwEnable) {
        this.throwOnHandlingFail = throwEnable;
        return this;
    }

    public FormFiller addViewExtractor(ViewValueExtractor... extractors) {
        for (ViewValueExtractor getter : extractors) {
            addViewExtractor(getter);
        }
        return this;
    }

    public FormFiller addViewExtractor(List<ViewValueExtractor> extractors) {
        for (ViewValueExtractor getter : extractors) {
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
     * usign default extractors only.
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
     * @param extractors
     */
    public static void fillWithView(Form form, View view,
                                    ViewValueExtractor<?, ?>... extractors) throws FormFieldError.ViewNotSupportedException {
        fillWithView(form, view, false, extractors);
    }

    /**
     * fill a form use view.
     *
     * @param form
     * @param view
     * @param editableOnly specify if only editable field should be flow on.
     * @param extractors
     */
    public static void fillWithView(Form form, View view, boolean editableOnly,
                                    ViewValueExtractor<?, ?>... extractors) {

        fillWithView(form, view, false,
                extractors != null ? Arrays.asList(extractors) : null);
    }

    /**
     * fill a form use view.
     *
     * @param form
     * @param view
     * @param policy
     */
    public static void fillWithView(Form form, View view, FillerPolicy policy) throws FormFieldError.ViewNotSupportedException {
        fillWithView(form, view, policy != null ? policy.editableOnly : false, policy != null ? policy.fieldExtractors : null);
    }

    /**
     * fill a form use view.
     *
     * @param form
     * @param view
     * @param editableOnly specify if only editable field should be flow on.
     * @param extractors
     */
    public static void fillWithView(Form form, View view, boolean editableOnly,
                                    List<ViewValueExtractor<?, ?>> extractors) throws FormFieldError.ViewNotSupportedException {
        FormFiller filler = FormFiller.using(form);
        List<FieldViewHandler<?, ?>> handlers = new ArrayList<FieldViewHandler<?, ?>>();
        if (extractors != null && extractors.size() > 0) {
            handlers.addAll(extractors);
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

    public final static ViewValueExtractor<Integer, AdapterView> EXTRACTOR_ADAPTER_VIEW_INDEX = new ViewValueExtractor<Integer, AdapterView>(Integer.class, AdapterView.class) {
        @Override
        public Integer getValue(AdapterView spinner) {
            return spinner.getSelectedItemPosition();
        }
    };
    public final static ViewValueExtractor<String, AdapterView> EXTRACTOR_ADAPTER_VIEW_TEXT = new ViewValueExtractor<String, AdapterView>(String.class, AdapterView.class) {
        @Override
        public String getValue(AdapterView spinner) {
            return FormTools.parseString(spinner.getSelectedItem());
        }
    };
    public final static FieldFiller<Spinner> EXTRACTOR_ADAPTER_VIEW_ENTITY = new FieldFiller<Spinner>(Spinner.class) {
        @Override
        public Object getValue(Spinner spinner) {
            return spinner.getSelectedItem();
        }
    };
    public final static FieldFiller<RadioGroup> EXTRACTOR_RADIO_GROUP_SELECTION_TEXT = new FieldFiller<RadioGroup>(RadioGroup.class) {
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

    public final static FieldFiller<RadioGroup> EXTRACTOR_RADIO_GROUP_SELECTION_TAG = new FieldFiller<RadioGroup>(RadioGroup.class) {
        @Override
        public Object getValue(RadioGroup v) {
            int selectionId = v.getCheckedRadioButtonId();
            View selectedView = v.findViewById(selectionId);
            if (selectedView instanceof RadioButton) {
                return selectedView.getTag();
            }
            return null;
        }
    };

    public final static FieldFiller<RadioGroup> EXTRACTOR_RADIO_GROUP_SELECTION_INDEX = new FieldFiller<RadioGroup>(RadioGroup.class) {
        @Override
        public Object getValue(RadioGroup v) {
            int selectionId = v.getCheckedRadioButtonId();
            List<View> viewChild = ViewUtil.getAllChildViews(v);
            int index = 0;
            for (View child : viewChild) {
                if (child instanceof RadioButton) {
                    if (selectionId == child.getId()) {
                        return index;
                    }
                    index++;
                }
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
                } else if (v instanceof CompoundButton) {
                    CompoundButton t = (CompoundButton) v;
                    return t.isChecked();
                } else if (v instanceof AdapterView) {
                    AdapterView t = (AdapterView) v;
                    return t.getSelectedItemPosition();
                } else if (v instanceof ImageView) {
                    ImageView t = (ImageView) v;
                    return t.getContentDescription();
                } else if (v instanceof RadioGroup) {
                    return EXTRACTOR_RADIO_GROUP_SELECTION_TEXT
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
        final List<ViewValueExtractor<?, ?>> fieldExtractors = new ArrayList<ViewValueExtractor<?, ?>>();

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
            return fieldExtractors;
        }

        public boolean isEditableOnly() {
            return editableOnly;
        }

        public FillerPolicy setEditableOnly(boolean editableOnly) {
            this.editableOnly = editableOnly;
            return this;
        }

        public FillerPolicy setExtractor(List<ViewValueExtractor<?, ?>> extractors) {
            this.fieldExtractors.clear();
            this.fieldExtractors.addAll(extractors);
            return this;
        }

        public FillerPolicy appendExtractor(ViewValueExtractor<?, ?> getter) {
            this.fieldExtractors.add(getter);
            return this;
        }

        public FillerPolicy appendExtractor(FieldFiller<?> getter) {
            this.fieldExtractors.add(getter);
            return this;
        }
    }
}
