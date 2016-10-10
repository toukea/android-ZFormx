package android.istat.base.forms.interfaces;

import android.istat.base.forms.Form;
import android.view.View;
/**
 * @author istat
 */
public interface FieldModel {
	public boolean onModelling(Form form, String fieldName, View view);
}
