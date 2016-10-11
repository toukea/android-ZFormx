package android.istat.base.forms;

import android.istat.base.forms.interfaces.FieldHandler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author istat
 */
public class FormMutator {
    Form form;
    boolean fillEditableOnly = false;

    FormMutator(Form form) {
        this.form = form;
    }

    void fetchMutableView(View formBaseView) {
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
                    fetchMutableView(view);
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
            mutateView(v);
        }
    }

    private void mutateView(View v) {
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

    public static FormMutator fetchMutableView(Form form, View view) {
        FormMutator binder = new FormMutator(form);
        binder.addFieldHandlers(null);
        binder.fetchMutableView(view);
        return binder;
    }

    public static FormMutator fetchMutableView(Form form, View view,
                                               boolean editableOnly) {
        FormMutator binder = new FormMutator(form);
        binder.addFieldHandlers(null);
        binder.fetchMutableView(view);
        return binder;
    }

    // -----------------------
    List<FieldHandler> fieldHandlers = new ArrayList<FieldHandler>();

    public static FormMutator fetchMutableView(Form form, View view,
                                               FieldHandler... fieldHandlers) {
        return fetchMutableView(form, view, false, fieldHandlers);
    }

    public static FormMutator fetchMutableView(Form form, View view,
                                               boolean editableOnly, FieldHandler... fieldHandlers) {
        return fetchMutableView(form, view, editableOnly,
                fieldHandlers != null ? Arrays.asList(fieldHandlers) : null);
    }

    public static FormMutator fetchMutableView(Form form, View view,
                                               List<FieldHandler> fieldHandlers) {
        return fetchMutableView(form, view, false, fieldHandlers);
    }

    public static FormMutator fetchMutableView(Form form, View view,
                                               boolean editableOnly, List<FieldHandler> fieldHandlers) {

        FormMutator binder = new FormMutator(form);
        binder.addFieldHandlers(fieldHandlers);
        binder.fetchMutableView(view);
        return binder;
    }

    void addFieldHandlers(List<FieldHandler> models) {
        FieldHandler defaultModel = new FieldHandler() {

            @Override
            public boolean onHandle(Form fieldName, String fieldValue, View v) {
                try {

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return false;
            }
        };
        fieldHandlers = models != null ? models : new ArrayList<FieldHandler>();
        fieldHandlers.add(defaultModel);
    }
}
