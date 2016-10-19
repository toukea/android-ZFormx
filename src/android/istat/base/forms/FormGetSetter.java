package android.istat.base.forms;

import android.istat.base.forms.tools.FormTools;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

/**
 * @author istat
 */
abstract class FormGetSetter {
    Form form;
    boolean editableOnlyGetSettable = false;
    final List<FieldValueGetSetter<?, ?>> fieldHandlers = new ArrayList<FieldValueGetSetter<?, ?>>();

    FormGetSetter(Form form) {
        this.form = form;
    }

    protected final void handleView(View formBaseView) {
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

    private void prepareViewHandler(List<FieldValueGetSetter<?, ?>> handlers) {
        if (handlers != null && handlers.size() > 0) {
            fieldHandlers.addAll(handlers);
        }
        fieldHandlers.addAll(getDefaultHandlers());
    }

    private void performViewHandling(View v) {
        if (fieldHandlers != null && fieldHandlers.size() > 0) {
            for (FieldValueGetSetter<?, ?> handler : fieldHandlers) {
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

    protected abstract List<FieldValueGetSetter<?, ?>> getDefaultHandlers();

    protected final void onHandleView(View view) {
        if (view != null) {
            if (view instanceof ViewGroup) {
                handleViewGroup((ViewGroup) view);
            } else {
                handleSingleView(view);
            }
        }
    }

    static abstract class FieldValueGetSetter<T, V extends View> {

        public FieldValueGetSetter() {

        }

        protected final boolean isHandleAble(View view) {
            Class<?> clazzView = getViewTypeClass();
            return (view.getClass().isAssignableFrom(clazzView)
                    || clazzView.isAssignableFrom(view.getClass()) || clazzView
                    .equals(view.getClass()));
        }
        @SuppressWarnings("unchecked")
        protected final Class<T> getFieldValueTypeClass() {
            try {
                String className = ((ParameterizedType) getClass()
                        .getGenericSuperclass()).getActualTypeArguments()[0]
                        .toString().replaceFirst("class", "").trim();
                Class<?> clazz = Class.forName(className);
                return (Class<T>) clazz;
            } catch (Exception e) {
                throw new IllegalStateException(
                        "Class is not parametrized with generic type!!! Please use extends <> ");
            }
        }

        @SuppressWarnings("unchecked")
        protected final Class<V> getViewTypeClass() {
            try {
                String className = ((ParameterizedType) getClass()
                        .getGenericSuperclass()).getActualTypeArguments()[0]
                        .toString().replaceFirst("class", "").trim();
                Class<?> clazz = Class.forName(className);
                return (Class<V>) clazz;
            } catch (Exception e) {
                throw new IllegalStateException(
                        "Class is not parametrized with generic type!!! Please use extends <> ");
            }
        }

        protected abstract boolean onHandle(Form form, String fieldName,
                                            View view);
    }
}
