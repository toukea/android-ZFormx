package istat.android.freedev.forms;

import istat.android.freedev.forms.tools.FormTools;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author istat
 */
abstract class FormViewHandler {
    public final static int MODE_VISIBLE_ONLY = 0, MODE_EDITABLE_ONLY = 1, MODE_ALL = 2, MODE_EMPTY_ONLY = 3;
    protected Form form;
    boolean accessibleOnlyGetSettable = false;
    boolean visibleOnlyGetSettable = false;
    boolean emptyOnlyGetSettable = false;
    private final List<FieldViewHandler<?, ?>> fieldHandlers = new ArrayList<FieldViewHandler<?, ?>>();
    private List<String> ignores = new ArrayList<String>();
    protected boolean throwOnHandlingFail = true;

    FormViewHandler() {
    }

    public FormViewHandler appendFieldToIgnore(String fieldNames) {
        Collections.addAll(ignores, fieldNames);
        return this;
    }

    public FormViewHandler appendFieldToIgnore(String... fieldNames) {
        if (fieldNames != null && fieldNames.length > 0) {
            Collections.addAll(ignores, fieldNames);
        }
        return this;
    }

    public FormViewHandler ignoreField(String fieldNames) {
        ignores.clear();
        Collections.addAll(ignores, fieldNames);
        return this;
    }

    public FormViewHandler ignoreFields(String... fieldNames) {
        if (fieldNames != null && fieldNames.length > 0) {
            ignores.clear();
            Collections.addAll(ignores, fieldNames);
        }
        return this;
    }

    protected final void handleView(View formBaseView, List<FieldViewHandler<?, ?>> fieldHandlers) {
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
        if (accessibleOnlyGetSettable && !v.isEnabled()) {
            return;
        }
        if (v != null && v.getTag() != null && !FormTools.isEmpty(v.getTag())) {
            performViewHandling(v);
        }
    }

    private void prepareViewHandler(List<FieldViewHandler<?, ?>> handlers) {
        if (handlers != null && handlers.size() > 0) {
            fieldHandlers.addAll(handlers);
        }
        fieldHandlers.addAll(getDefaultHandlers());
    }

    private void performViewHandling(View v) {
        if (fieldHandlers != null && fieldHandlers.size() > 0) {
            for (FieldViewHandler<?, ?> handler : fieldHandlers) {
                String tag = v.getTag() + "";
                if (ignores.contains(tag)) {
                    return;
                }
                boolean result = handler.onHandle(form, v.getTag() + "", v);
                if (result) {
                    return;
                }
            }
        }

        if (throwOnHandlingFail)
            throw new FormFieldError.ViewNotSupportedException("unsupported view for form autoBind::"
                    + v.getClass());
    }

    /**
     * Spécify if the handler will throw {@link istat.android.freedev.forms.FormFieldError.ViewNotSupportedException}
     * when it failed to handle a given tagged view.
     *
     * @param throwOnHandlingFail
     */
    public void setThrowOnHandlingFail(boolean throwOnHandlingFail) {
        this.throwOnHandlingFail = throwOnHandlingFail;
    }

    protected final void setAccessibleOnlyGetSettable(
            boolean accessibleOnlyGetSettable) {
        this.accessibleOnlyGetSettable = accessibleOnlyGetSettable;
    }

    protected final boolean isAccessibleOnlyGetSettable() {
        return accessibleOnlyGetSettable;
    }

    protected abstract List<FieldViewHandler<?, ?>> getDefaultHandlers();

    protected final void onHandleView(View view) {
        if (view != null) {
            if (view instanceof ViewGroup) {
                if (view instanceof AdapterView) {
                    handleSingleView(view);
                } else if (view.getTag() != null && !FormTools.isEmpty(view.getTag())) {
                    try {
                        handleSingleView(view);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    handleViewGroup((ViewGroup) view);
                }
            } else {
                handleSingleView(view);
            }
        }
    }

    static abstract class FieldViewHandler<ValueType, ViewType extends View> {
        Class<ValueType> valueType;
        Class<ViewType> viewType;
        List<String> acceptedField;

        public FieldViewHandler(Class<ValueType> valueType, Class<ViewType> viewType) {
            this.valueType = valueType;
            this.viewType = viewType;
        }

        public FieldViewHandler(Class<ValueType> valueType, Class<ViewType> viewType, String... acceptedField) {
            this(valueType, viewType);
            if (acceptedField != null) {
                this.acceptedField = Arrays.asList(acceptedField);
            }
        }

        @Deprecated
        public FieldViewHandler() {
            this.valueType = (Class<ValueType>) FormTools.getGenericTypeClass(this, 0);
            this.viewType = (Class<ViewType>) FormTools.getGenericTypeClass(this, 1);
        }

        protected boolean isHandleAble(View view) {
            Class<?> clazzView = getViewTypeClass();
            Class<?> clazzValue = getValueTypeClass();
            boolean valueTypeHandleAble = (view.getClass().isAssignableFrom(clazzValue)
                    || clazzValue.isAssignableFrom(view.getClass())
                    || clazzValue.equals(view.getClass()));

            boolean viewTypeHandleAble = (view.getClass().isAssignableFrom(clazzView)
                    || clazzView.isAssignableFrom(view.getClass())
                    || clazzView.equals(view.getClass()));

            if (valueTypeHandleAble && viewTypeHandleAble) {
                if (acceptedField != null && !acceptedField.isEmpty() && !FormTools.isEmpty(view.getTag())) {
                    return acceptedField.contains(view.getTag());
                }
                return true;
            }
            return false;
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
