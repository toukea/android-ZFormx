package android.istat.base.forms.interfaces;

import android.istat.base.forms.Form;
import android.istat.base.forms.FormState;
/**
 * @author istat
 */
public interface FormHandler {
	public void pullFrom(String url);

	public void pushTo(String url);

	public void clear();

	public Form getForm();

	public FormState checkUp();

	public void fillFromFormEntity(Form form);

	public void flowIntoFormEntity(Form form);

}
