package istat.android.freedev.forms.utils;

import android.widget.AdapterView;

import istat.android.freedev.forms.FormFiller;
import istat.android.freedev.forms.FormFlower;

/**
 * Created by istat on 18/01/17.
 */

public class AdapterViewFiller<T, Y extends AdapterView<?>> extends FormFiller.ViewExtractor<T, Y> {

    public AdapterViewFiller(Class<T> valueType, Class<Y> viewType) {
        super(valueType, viewType);
    }

    @Override
    public T getValue(Y y) {
        return null;
    }
}
