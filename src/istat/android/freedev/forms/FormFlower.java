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
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * @author istat
 */
public final class FormFlower extends FormGetSetter {
    private final List<FieldViewGetSetter<?, ?>> setters = new ArrayList<FieldViewGetSetter<?, ?>>();

    FormFlower(Form form) {
        super(form);
    }

    public void flowInto(View v) {
        handleView(v, setters);
    }

    public void flowInto(Window window) {
        flowInto(window.getDecorView());
    }

    public void flowInto(Activity activity) {
        flowInto(activity.getWindow().getDecorView());
    }

    public void flowInto(Dialog activity) {
        flowInto(activity.getWindow().getDecorView());
    }

    @SuppressLint("NewApi")
    public void flowInto(Fragment fragment) {
        flowInto(fragment.getView());
    }

    public <T> T flowInto(T obj) {
        return flowInto(obj, null);
    }

    public <T> T flowInto(T obj, ClassFormLoader loader) {
        form.flowInto(obj, loader);
        return obj;
    }

    public FormFlower setFlowEditableOnly(boolean state) {
        setEditableOnlyGetSettable(state);
        return this;
    }

    public FormFlower addViewSetter(FieldFlower flower) {
        setters.add(flower);
        return this;
    }

    public FormFlower addViewSetter(FieldViewSetter setter) {
        setters.add(setter);
        return this;
    }

    public FormFlower addViewSetter(FieldViewSetter... setters) {
        for (FieldViewSetter setter : setters) {
            addViewSetter(setter);
        }
        return this;
    }

    public FormFlower addViewSetter(List<FieldViewSetter> setters) {
        for (FieldViewSetter setter : setters) {
            addViewSetter(setter);
        }
        return this;
    }

    public static FormFlower use(Form form) {
        return new FormFlower(form);
    }

    /**
     * flow Form contain inside a view using a specific array of setters: {@link FieldFlower} or {@link FieldViewSetter}
     * using default Setters only.
     *
     * @param form
     * @param view
     */
    public static void flowIntoView(Form form, View view) {
        flowIntoView(form, view, false, new FieldViewSetter<?, ?>[0]);
    }

    /**
     * flow Form contain inside a view using a specific array of setters: {@link FieldFlower} or {@link FieldViewSetter}
     *
     * @param form
     * @param view
     * @param setters
     */
    public static void flowIntoView(Form form, View view,
                                    FieldViewSetter<?, ?>... setters) {
        flowIntoView(form, view, false, setters);
    }

    /**
     * * flow Form contain inside a view using a specific array of setters: {@link FieldFlower} or {@link FieldViewSetter}
     *
     * @param form
     * @param view
     * @param editableOnly specify if only editable field should be flow on.
     * @param setters
     */
    public static void flowIntoView(Form form, View view, boolean editableOnly,
                                    FieldViewSetter<?, ?>... setters) {
        flowIntoView(form, view, editableOnly,
                setters != null ? Arrays.asList(setters) : null);
    }

    /**
     * flow Form contain inside a view using a specific array of setters: {@link FieldFlower} or {@link FieldViewSetter}
     *
     * @param form
     * @param view
     * @param editableOnly
     * @param setters
     */
    public static void flowIntoView(Form form, View view, boolean editableOnly,
                                    List<FieldViewSetter<?, ?>> setters) {
        FormFlower flower = new FormFlower(form);
        List<FieldViewGetSetter<?, ?>> handlers = new ArrayList<FieldViewGetSetter<?, ?>>();
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
    public static abstract class FieldViewSetter<T, V extends View> extends
            FieldViewGetSetter<T, V> {

        public FieldViewSetter(Class<T> valueType, Class<V> viewType) {
            super(valueType, viewType);
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

    final FieldViewSetter<Integer, Spinner> SETTER_SPINNER_INDEX = new FieldViewSetter<Integer, Spinner>(Integer.class, Spinner.class) {

        @Override
        public void setValue(Integer entity, Spinner v) {
            v.setSelection(FormTools.parseInt(entity));
        }
    };
    final FieldViewSetter<String, Spinner> SETTER_SPINNER_CONTAINT = new FieldViewSetter<String, Spinner>(String.class, Spinner.class) {

        @Override
        public void setValue(String entity, Spinner spinner) {

        }
    };

    public static abstract class FieldFlower<V extends View> extends
            FieldViewSetter<Object, V> {

        public FieldFlower(Class<V> viewType) {
            super(Object.class, viewType);
        }
    }

    final FieldFlower<TextView> SETTER_TEXT_VIEW_TEXT = new FieldFlower<TextView>(TextView.class) {

        @Override
        public void setValue(Object value, TextView v) {
            v.setText(FormTools.parseString(value));
        }
    };
    public final static FieldFlower<CheckBox> SETTER_CHECKBOX_STATE = new FieldFlower<CheckBox>(CheckBox.class) {

        @Override
        public void setValue(Object value, CheckBox v) {

            v.setChecked(FormTools.parseBoolean(value));

        }
    };
    public final static FieldFlower<RadioButton> SETTER_RADIO_BUTTON_STATE = new FieldFlower<RadioButton>(RadioButton.class) {

        @Override
        public void setValue(Object value, RadioButton v) {
            v.setChecked(FormTools.parseBoolean(value));
        }
    };

    public final static FieldFlower<RadioGroup> SETTER_RADIO_GROUP_SELECTED_RADIATION_INDEX = new FieldFlower<RadioGroup>(RadioGroup.class) {

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
    public final static FieldViewSetter<Integer, ImageView> SETTER_IMAGE_VIEW_INT_RESOURCE = new FieldViewSetter<Integer, ImageView>(Integer.class, ImageView.class) {


        @Override
        public void setValue(Integer entity, ImageView imageView) {
            try {
                imageView.setImageResource(entity);
            } catch (Exception e) {

            }
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
                add(SETTER_SPINNER_INDEX);
                add(SETTER_TEXT_VIEW_TEXT);
                add(SETTER_CHECKBOX_STATE);
                add(SETTER_RADIO_BUTTON_STATE);
                add(SETTER_RADIO_GROUP_SELECTED_RADIATION_INDEX);
                add(SETTER_IMAGE_VIEW_INT_RESOURCE);
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
