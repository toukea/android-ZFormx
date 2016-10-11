package android.istat.base.forms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.istat.base.forms.interfaces.FieldHandler;
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
public class FormFlower {
    Form form;
    boolean fillEditableOnly = false;

    FormFlower(Form form) {
        this.form = form;
    }

    void flowIntoView(View formBaseView) {
        if (formBaseView != null) {
            if (formBaseView instanceof ViewGroup) {
                traitViewAsViewGroup((ViewGroup) formBaseView);
            } else {
                traitViewAsSimpleView(formBaseView);
            }
        }
    }

    private void traitViewAsViewGroup(ViewGroup v) {
        String[] fields = form.getFieldNames();
        for (String field : fields) {
            View view = v.findViewWithTag(field);
            if (view != null) {
                if (view instanceof ViewGroup) {
                    flowIntoView(view);
                } else {
                    traitViewAsSimpleView(view);
                }
            }
        }

    }

    private void traitViewAsSimpleView(View v) {
        if (fillEditableOnly && !v.isEnabled()) {
            return;
        }
        if (v.getTag() != null && !TextUtils.isEmpty(v.getTag() + "")) {
            flowView(v);
        }
    }

    private void flowView(View v) {
        if (fieldHandlers != null && fieldHandlers.size() > 0) {
            for (FieldHandler handler : fieldHandlers) {
                boolean result = handler.onHandle(form, v.getTag() + "", v);
                if (result) {
                    return;
                }
            }
        }
        throw new RuntimeException("unsuported view for form autoBind::"
                + v.getClass());
    }

    private void flowTextView(View v) {
        TextView t = (TextView) v;
        String value = form.optString(v.getTag() + "");
        if (!TextUtils.isEmpty(value)) {
            t.setText(value);
        }
    }

    private void flowCheckBox(View v) {
        CheckBox t = (CheckBox) v;
        String value = form.optString(v.getTag() + "");
        t.setChecked(FormTools.parseBoolean(value));
    }

    private void flowSpinner(View v) {
        Spinner t = (Spinner) v;
        String value = form.optString(v.getTag() + "");
        t.setSelection(FormTools.parseInt(value));
    }

    private void flowRadioButton(View v) {
        TextView t = (TextView) v;
        String value = form.optString(v.getTag() + "");
        t.setText(value);
    }

    private void flowRadioGroup(View v) {
        RadioButton t = (RadioButton) v;
        String value = form.optString(v.getTag() + "");
        t.setChecked(FormTools.parseBoolean(value));
    }

    public static FormFlower flowIntoView(Form form, View view) {
        FormFlower binder = new FormFlower(form);
        binder.addFieldHandlers(null);
        binder.flowIntoView(view);
        return binder;
    }

    public static FormFlower flowIntoView(Form form, View view,
                                          boolean editableOnly) {
        FormFlower binder = new FormFlower(form);
        binder.addFieldHandlers(null);
        binder.flowIntoView(view);
        return binder;
    }

    // -----------------------
    List<FieldHandler> fieldHandlers = new ArrayList<FieldHandler>();

    public static FormFlower flowIntoView(Form form, View view,
                                          FieldHandler... fieldHandlers) {
        return flowIntoView(form, view, false, fieldHandlers);
    }

    public static FormFlower flowIntoView(Form form, View view,
                                          boolean editableOnly, FieldHandler... fieldHandlers) {
        return flowIntoView(form, view, editableOnly,
                fieldHandlers != null ? Arrays.asList(fieldHandlers) : null);
    }

    public static FormFlower flowIntoView(Form form, View view,
                                          List<FieldHandler> fieldHandlers) {
        return flowIntoView(form, view, false, fieldHandlers);
    }

    public static FormFlower flowIntoView(Form form, View view,
                                          boolean editableOnly, List<FieldHandler> fieldHandlers) {

        FormFlower binder = new FormFlower(form);
        binder.addFieldHandlers(fieldHandlers);
        binder.flowIntoView(view);
        return binder;
    }

    void addFieldHandlers(List<FieldHandler> models) {
        FieldHandler defaultModel = new FieldHandler() {

            @Override
            public boolean onHandle(Form fieldName, String fieldValue, View v) {
                try {
                    if (v instanceof TextView) {
                        flowTextView(v);
                        return true;
                    } else if (v instanceof CheckBox) {
                        flowCheckBox(v);
                        return true;
                    } else if (v instanceof Spinner) {
                        flowSpinner(v);
                        return true;
                    } else if (v instanceof RadioButton) {
                        flowRadioButton(v);
                        return true;
                    } else if (v instanceof RadioGroup) {
                        flowRadioGroup(v);
                        return true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return false;
            }
        };
        fieldHandlers = models != null ? models : new ArrayList<FieldHandler>();
        fieldHandlers.add(defaultModel);
    }

//    public static abstract class FlowerHandler<T> implements FieldHandler {
//        public abstract void onFlowInTo(T entity, View v);
//
//        @Override
//        public boolean onHandle(Form form, String fieldName, View view) {
//            //onFlowInTo(form.op)
//            return false;
//        }
//    }
}
