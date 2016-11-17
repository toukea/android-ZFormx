package istat.android.freedev.forms;

import istat.android.freedev.forms.tools.FormTools;

import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author istat
 */
abstract class FormGetSetter {
    public final static int MODE_VISIBLE_ONLY = 0, MODE_EDITABLE_ONLY = 1, MODE_ALL = 2, MODE_EMPTY_ONLY = 3;
    Form form;
    boolean editableOnlyGetSettable = false;
    boolean visibleOnlyGetSettable = false;
    boolean emptyOnlyGetSettable = false;
    private final List<FieldViewGetSetter<?, ?>> fieldHandlers = new ArrayList<FieldViewGetSetter<?, ?>>();

    FormGetSetter(Form form) {
        this.form = form;
    }

    protected final void handleView(View formBaseView, List<FieldViewGetSetter<?, ?>> fieldHandlers) {
        prepareViewHandler(fieldHandlers);
        if (formBaseView != null) {
            if (formBaseView instanceof ViewGroup) {
                handleViewGroup((ViewGroup) formBaseView);
            } else {
                handleSingleView(formBaseView);
            }
        }
    }

    protected abstract void handleViewGroup(ViewGroup v);

    protected final void handleSingleView(View v) {
        if (editableOnlyGetSettable && !v.isEnabled()) {
            return;
        }
        if (v != null && v.getTag() != null && !FormTools.isEmpty(v.getTag())) {
            performViewHandling(v);
        }
    }

    private void prepareViewHandler(List<FieldViewGetSetter<?, ?>> handlers) {
        if (handlers != null && handlers.size() > 0) {
            fieldHandlers.addAll(handlers);
        }
        fieldHandlers.addAll(getDefaultHandlers());
    }

    private void performViewHandling(View v) {
        if (fieldHandlers != null && fieldHandlers.size() > 0) {
            for (FieldViewGetSetter<?, ?> handler : fieldHandlers) {
                boolean result = handler.onHandle(form, v.getTag() + "", v);
                if (result) {
                    return;
                }
            }
        }
        throw new RuntimeException("unsupported view for form autoBind::"
                + v.getClass());
    }

    protected final void setEditableOnlyGetSettable(
            boolean editableOnlyGetSettable) {
        this.editableOnlyGetSettable = editableOnlyGetSettable;
    }

    protected final boolean isEditableOnlyGetSettable() {
        return editableOnlyGetSettable;
    }

    protected abstract List<FieldViewGetSetter<?, ?>> getDefaultHandlers();

    protected final void onHandleView(View view) {
        if (view != null) {
            if (view instanceof ViewGroup) {
                handleViewGroup((ViewGroup) view);
            } else {
                handleSingleView(view);
            }
        }
    }

    static abstract class FieldViewGetSetter<ValueType, ViewType extends View> {
        Class<ValueType> valueType;
        Class<ViewType> viewType;

        public FieldViewGetSetter(Class<ValueType> valueType, Class<ViewType> viewType) {
            this.valueType = valueType;
            this.viewType = viewType;
        }

        protected boolean isHandleAble(View view) {
            Class<?> clazzView = getViewTypeClass();
            return (view.getClass().isAssignableFrom(clazzView)
                    || clazzView.isAssignableFrom(view.getClass())
                    || clazzView.equals(view.getClass()));
        }

        @SuppressWarnings("unchecked")
        final Class<ValueType> getValueTypeClass() {
            return valueType;
        }

        @SuppressWarnings("unchecked")
        final Class<ViewType> getViewTypeClass() {
            return viewType;
        }

        protected abstract boolean onHandle(Form form, String fieldName,
                                            View view);
    }
}
