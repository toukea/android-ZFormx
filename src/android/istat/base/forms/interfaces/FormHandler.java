package android.istat.base.forms.interfaces;


import android.istat.base.forms.Form;
import android.istat.base.forms.FormState;

public interface FormHandler {
	public void pullFrom(String url);

	public void submit(String url);

	public Form getForm();

	public FormState checkUp();

	public void fillFromFormEntity(Form form);

	public void flowIntoFormEntity(Form form);

	public void clear();
}
