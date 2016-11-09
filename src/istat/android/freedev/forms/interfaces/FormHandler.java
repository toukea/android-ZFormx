package istat.android.freedev.forms.interfaces;

import istat.android.freedev.forms.Form;
import istat.android.freedev.forms.FormState;
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
