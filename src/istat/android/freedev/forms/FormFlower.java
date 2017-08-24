package istat.android.freedev.forms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import istat.android.freedev.forms.tools.FormTools;
import istat.android.freedev.forms.utils.ClassFormLoader;
import istat.android.freedev.forms.utils.ViewUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * @author istat
 */
public final class FormFlower extends FormViewHandler {
    private final List<FieldViewHandler<?, ?>> setters = new ArrayList<FieldViewHandler<?, ?>>();

    public FormFlower() {

    }

    public FormFlower use(Form form) {
        this.form = form;
        return this;
    }

    public void flowInto(View v) throws FormFieldError.ViewNotSupportedException {
        handleView(v, setters);
    }

    public void flowInto(Window window) throws FormFieldError.ViewNotSupportedException {
        flowInto(window.getDecorView());
    }

    public void flowInto(Activity activity) throws FormFieldError.ViewNotSupportedException {
        flowInto(activity.getWindow().getDecorView());
    }

    public void flowInto(Dialog activity) throws FormFieldError.ViewNotSupportedException {
        flowInto(activity.getWindow().getDecorView());
    }

    @SuppressLint("NewApi")
    public void flowInto(Fragment fragment) throws FormFieldError.ViewNotSupportedException {
        flowInto(fragment.getView());
    }

    public <T> T flowInto(T obj) {
        return flowInto(obj, null);
    }

    public <T> T flowInto(T obj, ClassFormLoader loader) {
        form.flowInto(obj, loader);
        return obj;
    }

    public FormFlower setFlowAccessibleOnly(boolean state) {
        setAccessibleOnlyGetSettable(state);
        return this;
    }

    public FormFlower addViewInjector(FieldFlower flower) {
        setters.add(flower);
        return this;
    }

    public FormFlower addViewInjector(ViewValueInjector setter) {
        setters.add(setter);
        return this;
    }

    public FormFlower addViewInjector(ViewValueInjector... setters) {
        for (ViewValueInjector setter : setters) {
            addViewInjector(setter);
        }
        return this;
    }

    public FormFlower addViewInjectors(List<ViewValueInjector> setters) {
        for (ViewValueInjector setter : setters) {
            addViewInjector(setter);
        }
        return this;
    }

    public static FormFlower using(Form form) {
        FormFlower flower = new FormFlower();
        flower.use(form);
        return flower;
    }

    /**
     * flow Form contain inside a view using a specific array of setters: {@link FieldFlower} or {@link ViewValueInjector}
     * using default Setters only.
     *
     * @param form
     * @param view
     */
    public static void flowIntoView(Form form, View view) {
        flowIntoView(form, view, false, new ViewValueInjector<?, ?>[0]);
    }

    /**
     * flow Form contain inside a view using a specific array of setters: {@link FieldFlower} or {@link ViewValueInjector}
     *
     * @param form
     * @param view
     * @param setters
     */
    public static void flowIntoView(Form form, View view,
                                    ViewValueInjector<?, ?>... setters) {
        flowIntoView(form, view, false, setters);
    }

    /**
     * * flow Form contain inside a view using a specific array of setters: {@link FieldFlower} or {@link ViewValueInjector}
     *
     * @param form
     * @param view
     * @param editableOnly specify if only editable field should be flow on.
     * @param setters
     */
    public static void flowIntoView(Form form, View view, boolean editableOnly,
                                    ViewValueInjector<?, ?>... setters) {
        flowIntoView(form, view, editableOnly,
                setters != null ? Arrays.asList(setters) : null);
    }

    /**
     * flow Form contain inside a view using a specific array of setters: {@link FieldFlower} or {@link ViewValueInjector}
     *
     * @param form
     * @param view
     * @param editableOnly
     * @param setters
     */
    public static void flowIntoView(Form form, View view, boolean editableOnly,
                                    List<ViewValueInjector<?, ?>> setters) {
        FormFlower flower = FormFlower.using(form);
        List<FieldViewHandler<?, ?>> handlers = new ArrayList<FieldViewHandler<?, ?>>();
        if (setters != null && setters.size() > 0) {
            handlers.addAll(setters);
        }
        flower.setAccessibleOnlyGetSettable(editableOnly);
        flower.handleView(view, handlers);

    }

    /**
     * @param <T>
     * @param <V>
     */
    public static abstract class ViewValueInjector<T, V extends View> extends
            FieldViewHandler<T, V> {

        public ViewValueInjector(Class<T> valueType, Class<V> viewType) {
            super(valueType, viewType);
        }

        public ViewValueInjector(Class<T> valueType, Class<V> viewType, String... acceptedField) {
            super(valueType, viewType, acceptedField);
        }

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

    final ViewValueInjector<Integer, AdapterView> INJECTOR_ADAPTER_VIEW_INDEX = new ViewValueInjector<Integer, AdapterView>(Integer.class, AdapterView.class) {

        @Override
        public void setValue(Integer entity, AdapterView v) {
            v.setSelection(FormTools.parseInt(entity));
        }
    };

    public static abstract class FieldFlower<V extends View> extends
            ViewValueInjector<Object, V> {

        public FieldFlower(Class<V> viewType) {
            super(Object.class, viewType);
        }
    }

    //CheckedTextView
    public final static ViewValueInjector<Boolean, CheckedTextView> INJECTOR_CHECKED_TEXT_VIEW = new ViewValueInjector<Boolean, CheckedTextView>(Boolean.class, CheckedTextView.class) {

        @Override
        public void setValue(Boolean value, CheckedTextView v) {
            v.setChecked(value);
        }
    };

    final FieldFlower<TextView> INJECTOR_TEXT_VIEW_TEXT = new FieldFlower<TextView>(TextView.class) {

        @Override
        public void setValue(Object value, TextView v) {
            v.setText(FormTools.parseString(value));
        }
    };


    public final static FieldFlower<CompoundButton> INJECTOR_COMPOUND_BUTTON_STATE = new FieldFlower<CompoundButton>(CompoundButton.class) {

        @Override
        public void setValue(Object value, CompoundButton v) {
            v.setChecked(FormTools.parseBoolean(value));
        }
    };

    public final static FieldFlower<RadioGroup> INJECTOR_RADIO_GROUP_SELECTED_RADIO_ACTION_INDEX = new FieldFlower<RadioGroup>(RadioGroup.class) {

        @Override
        public void setValue(Object value, RadioGroup v) {
            int selectionIndex = FormTools.parseInt(value);
            List<View> viewChild = ViewUtil.getAllChildViews(v);
            int index = 0;
            for (View child : viewChild) {
                if (child instanceof RadioButton) {
                    if (index == selectionIndex) {
                        RadioButton button = (RadioButton) child;
                        button.setChecked(true);
                        break;
                    }
                    index++;
                }
            }
        }
    };

    public final static FieldFlower<RadioGroup> INJECTOR_RADIO_GROUP_SELECTED_RADIO_ACTION_TAG = new FieldFlower<RadioGroup>(RadioGroup.class) {

        @Override
        public void setValue(Object value, RadioGroup v) {
            String tag = FormTools.parseString(value);
            if (!TextUtils.isEmpty(tag)) {
                View viewTagged = v.findViewWithTag(tag);
                if (viewTagged instanceof RadioButton) {
                    RadioButton button = (RadioButton) viewTagged;
                    button.setSelected(true);
                }
            }
        }
    };

    public final static ViewValueInjector<Integer, ProgressBar> INJECTOR_PROGRESS_BAR = new ViewValueInjector<Integer, ProgressBar>(Integer.class, ProgressBar.class) {


        @Override
        public void setValue(Integer entity, ProgressBar v) {
            try {
                v.setProgress(entity);
            } catch (Exception e) {

            }
        }
    };

    public final static ViewValueInjector<Integer, ImageView> INJECTOR_IMAGE_VIEW_INT_RESOURCE = new ViewValueInjector<Integer, ImageView>(Integer.class, ImageView.class) {


        @Override
        public void setValue(Integer entity, ImageView imageView) {
            try {
                imageView.setImageResource(entity);
            } catch (Exception e) {

            }
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
                add(INJECTOR_ADAPTER_VIEW_INDEX);
                add(INJECTOR_TEXT_VIEW_TEXT);
                add(INJECTOR_CHECKED_TEXT_VIEW);
                add(INJECTOR_PROGRESS_BAR);
                add(INJECTOR_COMPOUND_BUTTON_STATE);
                add(INJECTOR_RADIO_GROUP_SELECTED_RADIO_ACTION_INDEX);
                add(INJECTOR_IMAGE_VIEW_INT_RESOURCE);
            }
        };
    }

    public FormFlower throwViewNotSupported(boolean throwNotSupported) {
        this.throwOnHandlingFail = throwNotSupported;
        return this;
    }

    protected final void handleViewGroup(ViewGroup v) {
        String[] fields = form.getFieldNames();
        for (String field : fields) {
            View view = v.findViewWithTag(field);
            onHandleView(view);
        }
    }
}
