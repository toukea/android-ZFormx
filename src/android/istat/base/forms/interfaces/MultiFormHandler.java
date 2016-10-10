package android.istat.base.forms.interfaces;

import java.util.List;

import android.istat.base.forms.Form;
import android.istat.base.forms.FormCheckSummary;
import android.istat.base.forms.FormState;
/**
 * @author istat
 */
public interface MultiFormHandler {
	public void pullFroms(String url);

	public void pushForms(String url);

	public List<Form> getForms();

	public FormCheckSummary checkUp(boolean breakIfError);

	public FormState checkUp(int index);

	public void fillFromFormEntities(Form... forms);

	public void flowIntoFormEntities(Form... forms);

	public void clear();

	public void clear(int index);
}
