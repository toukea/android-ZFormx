package android.istat.base.forms;

import android.istat.base.forms.interfaces.FieldHandler;
import android.istat.base.forms.interfaces.FormHandler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

/**
 * @author istat
 */
abstract class FormGetSetter {
    protected Form form;
    protected boolean modifyEditableOnly = false;

    FormGetSetter(Form form) {
        this.form = form;
    }

    protected final void mutateView(View formBaseView) {
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
                    mutateView(view);
                } else {
                    traitViewAsSimpleView(view);
                }
            }
        }

    }

    private void traitViewAsSimpleView(View v) {
        if (modifyEditableOnly && !v.isEnabled()) {
            return;
        }
        if (v.getTag() != null && !TextUtils.isEmpty(v.getTag() + "")) {
            performMutation(v);
        }
    }

    private void performMutation(View v) {
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

    protected final List<FieldHandler> fieldHandlers = new ArrayList<FieldHandler>();

    protected void addAllFieldHandlers(List<FieldHandler> handlers) {
        fieldHandlers.addAll(handlers);
        fieldHandlers.add(getDefaultHandler());
    }

    protected final void setModifyEditableOnly(boolean modifyEditableOnly) {
        this.modifyEditableOnly = modifyEditableOnly;
    }

    protected final boolean isModifyEditableOnly() {
        return modifyEditableOnly;
    }

    protected abstract FieldHandler getDefaultHandler();

    static abstract class FieldValueGetSetter implements FieldHandler {
        boolean override = false;

        public FieldValueGetSetter() {

        }

        public FieldValueGetSetter(boolean override) {
            this.override = override;
        }

        protected final Class<?> getFieldValueTypeClass() {
            try {
                String className = ((ParameterizedType) getClass()
                        .getGenericSuperclass()).getActualTypeArguments()[0]
                        .toString().replaceFirst("class", "").trim();
                Class<?> clazz = Class.forName(className);
                Log.d("FieldValueGetSetter", "getFieldValueTypeClass::clazz[0]=" + clazz);
                return clazz;
            } catch (Exception e) {
                throw new IllegalStateException(
                        "Class is not parametrized with generic type!!! Please use extends <> ");
            }
        }

        protected final <T extends View> Class<T> getViewTypeClass() {
            try {
                String className = ((ParameterizedType) getClass()
                        .getGenericSuperclass()).getActualTypeArguments()[1]
                        .toString().replaceFirst("class", "").trim();
                Class<?> clazz = Class.forName(className);
                Log.d("FieldValueGetSetter", "getViewTypeClass::clazz[0]=" + clazz);
                return (Class<T>) clazz;
            } catch (Exception e) {
                throw new IllegalStateException(
                        "Class is not parametrized with generic type!!! Please use extends <> ");
            }
        }

        @Override
        public boolean onHandle(Form form, String fieldName, View view) {
            return this.override;
        }
    }
}
