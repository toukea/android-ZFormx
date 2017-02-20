package istat.android.freedev.forms.tools;

import android.widget.Adapter;
import android.widget.AdapterView;

import java.lang.reflect.Field;
import java.util.List;

import istat.android.freedev.forms.FormFiller;

/**
 * Created by istat on 18/01/17.
 */
/*
T as Value
Y as View
 */
public abstract class AdapterViewValueExtractors {

    public static FormFiller.ViewValueExtractor<String, AdapterView> createCharSequenceExtractor(String... accept) {
        return createCharSequenceExtractor(null, accept);
    }

    public static FormFiller.ViewValueExtractor<String, AdapterView> createCharSequenceExtractor(final String defaultValue, String... accept) {
        return new FormFiller.ViewValueExtractor<String, AdapterView>(String.class, AdapterView.class, accept) {
            @Override
            public String getValue(AdapterView adapterView) {
                Adapter adapter = adapterView.getAdapter();
                if (adapter != null) {
                    int selectedIndex = adapterView.getSelectedItemPosition();
                    Object selectedItem = adapter.getItem(selectedIndex);
                    return selectedItem != null ? selectedItem.toString() : defaultValue;
                }
                return defaultValue;
            }
        };
    }

    public static FormFiller.ViewValueExtractor<Integer, AdapterView> createIndexExtractor(String... accept) {
        return new FormFiller.ViewValueExtractor<Integer, AdapterView>(Integer.class, AdapterView.class, accept) {
            @Override
            public Integer getValue(AdapterView adapterView) {
                Adapter adapter = adapterView.getAdapter();
                if (adapter != null) {
                    int selectedIndex = adapterView.getSelectedItemPosition();
                    return selectedIndex;
                }
                return -1;
            }
        };
    }

    public static FormFiller.ViewValueExtractor<String, AdapterView> createListObjectFieldExtractor(final List<?> list, final String fieldName, String... accept) {
        return createListObjectFieldExtractor(list, fieldName, null, accept);
    }

    public static FormFiller.ViewValueExtractor<String, AdapterView> createListObjectFieldExtractor(final List<?> list, final String fieldName, final String defaultValue, String... accept) {
        return new FormFiller.ViewValueExtractor<String, AdapterView>(String.class, AdapterView.class, accept) {
            @Override
            public String getValue(AdapterView adapterView) {
                Adapter adapter = adapterView.getAdapter();
                if (adapter != null) {
                    int selectedIndex = adapterView.getSelectedItemPosition();
                    Object obj = list.get(selectedIndex);
                    try {
                        Field field = obj.getClass().getField(fieldName);
                        field.setAccessible(true);
                        Object value = field.get(obj);
                        return value.toString();
                    } catch (Exception e) {

                    }
                }
                return defaultValue;
            }
        };
    }

    public static FormFiller.ViewValueExtractor<String, AdapterView> createAdapterObjectFieldExtractor(final String fieldName, final String defaultValue, String... accept) {
        return new FormFiller.ViewValueExtractor<String, AdapterView>(String.class, AdapterView.class, accept) {
            @Override
            public String getValue(AdapterView adapterView) {
                Adapter adapter = adapterView.getAdapter();
                if (adapter != null) {
                    int selectedIndex = adapterView.getSelectedItemPosition();
                    Object obj = adapter.getItem(selectedIndex);
                    try {
                        Field field = obj.getClass().getField(fieldName);
                        field.setAccessible(true);
                        Object value = field.get(obj);
                        return value.toString();
                    } catch (Exception e) {

                    }
                }
                return defaultValue;
            }
        };
    }
}
