package istat.android.freedev.forms.tools;

import android.widget.Adapter;
import android.widget.AdapterView;

import java.lang.reflect.Field;
import java.util.List;

import istat.android.freedev.forms.FormFlower;

/**
 * Created by istat on 18/01/17.
 */

public class AdapterViewValueInjectors {
    public static FormFlower.ViewValueInjector<String, AdapterView> createCharSequenceInjector(String... accept) {
        return createCharSequenceInjector(null, accept);
    }

    public static FormFlower.ViewValueInjector<String, AdapterView> createCharSequenceInjector(final String defaultValue, String... accept) {
        return new FormFlower.ViewValueInjector<String, AdapterView>(String.class, AdapterView.class, accept) {
            @Override
            public void setValue(String value, AdapterView adapterView) {
                if (value == null) {
                    return;
                }
                Adapter adapter = adapterView.getAdapter();
                if (adapter != null) {
                    Object selectedItem;
                    for (int i = 0; i < adapter.getCount(); i++) {
                        selectedItem = adapter.getItem(i);
                        if (value.equals(selectedItem)) {
                            adapterView.setSelection(i);
                            return;
                        }
                    }
                }
            }
        };
    }

    public static FormFlower.ViewValueInjector<Integer, AdapterView> createIndexInjector(String... accept) {
        return new FormFlower.ViewValueInjector<Integer, AdapterView>(Integer.class, AdapterView.class, accept) {
            @Override
            public void setValue(Integer value, AdapterView adapterView) {
                Adapter adapter = adapterView.getAdapter();
                if (adapter.getCount() > value) {
                    adapterView.setSelection(value);
                }
            }
        };
    }

    public static FormFlower.ViewValueInjector<String, AdapterView> createListObjectFieldInjector(final List<?> list, final String fieldName, String... accept) {
        return createListObjectFieldInjector(list, fieldName, null, accept);
    }

    public static FormFlower.ViewValueInjector<String, AdapterView> createListObjectFieldInjector(final List<?> list, final String fieldName, final String defaultValue, String... accept) {
        return new FormFlower.ViewValueInjector<String, AdapterView>(String.class, AdapterView.class, accept) {
            @Override
            public void setValue(String value, AdapterView adapterView) {
                if (value == null) {
                    return;
                }
                Adapter adapter = adapterView.getAdapter();
                if (adapter != null) {
                    Object selectedItem;
                    for (int i = 0; i < adapter.getCount(); i++) {
                        selectedItem = list.get(i);
                        try {
                            Field field = selectedItem.getClass().getField(fieldName);
                            field.setAccessible(true);
                            Object fieldValue = field.get(selectedItem);
                            if (value.equals(fieldValue)) {
                                adapterView.setSelection(i);
                                return;
                            }
                        } catch (Exception e) {

                        }
                    }
                }
            }
        };
    }

    public static FormFlower.ViewValueInjector<String, AdapterView> createAdapterObjectFieldInjector(final String fieldName, String... accept) {
        return createAdapterObjectFieldInjector(fieldName, null, accept);
    }

    public static FormFlower.ViewValueInjector<String, AdapterView> createAdapterObjectFieldInjector(final String fieldName, final String defaultValue, String... accept) {
        return new FormFlower.ViewValueInjector<String, AdapterView>(String.class, AdapterView.class, accept) {
            @Override
            public void setValue(String value, AdapterView adapterView) {
                Adapter adapter = adapterView.getAdapter();
                if (adapter != null) {
                    Object selectedItem;
                    for (int i = 0; i < adapter.getCount(); i++) {
                        selectedItem = adapter.getItem(i);
                        try {
                            Field field = selectedItem.getClass().getField(fieldName);
                            field.setAccessible(true);
                            Object fieldValue = field.get(selectedItem);
                            if (value.equals(fieldValue)) {
                                adapterView.setSelection(i);
                                return;
                            }
                        } catch (Exception e) {

                        }
                    }
                }
            }
        };
    }
}
