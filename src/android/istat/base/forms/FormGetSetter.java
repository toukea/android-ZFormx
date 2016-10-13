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
    protected Form form;
    protected boolean editableOnlyGetSettable = false;
    final List<FieldValueGetSetter> fieldHandlers = new ArrayList<FieldValueGetSetter>();

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
        if (v.getTag() != null && !FormTools.isEmpty(v.getTag())) {
            performViewHandling(v);
        }
    }

    private void prepareViewHandler(List<FieldValueGetSetter> handlers) {
        if (handlers != null && handlers.size() > 0) {
            fieldHandlers.addAll(handlers);
        }
        fieldHandlers.add(getDefaultHandler());
    }

    private void performViewHandling(View v) {
        if (fieldHandlers != null && fieldHandlers.size() > 0) {
            for (FieldValueGetSetter handler : fieldHandlers) {
                boolean result = handler.onHandle(form, v.getTag() + "", v);
                if (result) {
                    return;
                }
            }
        }
        throw new RuntimeException("unsupported view for form autoBind::"
                + v.getClass());
    }

    protected final void setEditableOnlyGetSettable(boolean editableOnlyGetSettable) {
        this.editableOnlyGetSettable = editableOnlyGetSettable;
    }

    protected final boolean isEditableOnlyGetSettable() {
        return editableOnlyGetSettable;
    }

    protected abstract FieldValueGetSetter getDefaultHandler();

    protected final void applyGetSetter(View view) {
        if (view != null) {
            if (view instanceof ViewGroup) {
                handleView(view);
            } else {
                handleSingleView(view);
            }
        }
    }

    static abstract class FieldValueGetSetter {
        boolean override = false;

        public FieldValueGetSetter() {

        }

        public FieldValueGetSetter(boolean override) {
            this.override = override;
        }

        protected final boolean isHandleAble(View view) {
            Class<?> clazzView = getViewTypeClass();
            return (view.getClass().isAssignableFrom(clazzView)
                    || clazzView.isAssignableFrom(view.getClass()) || clazzView
                    .equals(view.getClass()));
        }

        protected final Class<?> getFieldValueTypeClass() {
            try {
                String className = ((ParameterizedType) getClass()
                        .getGenericSuperclass()).getActualTypeArguments()[0]
                        .toString().replaceFirst("class", "").trim();
                Class<?> clazz = Class.forName(className);
                return clazz;
            } catch (Exception e) {
                throw new IllegalStateException(
                        "Class is not parametrized with generic type!!! Please use extends <> ");
            }
        }

        @SuppressWarnings("unchecked")
        protected final <T> Class<T> getViewTypeClass() {
            try {
                String className = ((ParameterizedType) getClass()
                        .getGenericSuperclass()).getActualTypeArguments()[1]
                        .toString().replaceFirst("class", "").trim();
                Class<?> clazz = Class.forName(className);
                return (Class<T>) clazz;
            } catch (Exception e) {
                throw new IllegalStateException(
                        "Class is not parametrized with generic type!!! Please use extends <> ");
            }
        }

//        protected final boolean handle(Form form, String fieldName, View view) {
//            if(view!=null){
//                view.post(new Runnable(){
//
//                    @Override
//                    public void run() {
//
//                    }
//                });
//            }
//        }

        protected boolean onHandle(Form form, String fieldName, View view) {
            return this.override;
        }
    }
}
