package android.istat.base.forms.interfaces;

import android.istat.base.forms.Form;
import android.view.View;
/**
 * @author istat
 */
public interface FieldHandler {
	public boolean onHandle(Form form, String fieldName, View view);
}
