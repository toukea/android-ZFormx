package istat.android.freedev.forms.interfaces;

import java.util.List;

import istat.android.freedev.forms.Form;
import istat.android.freedev.forms.FormCheckSummary;
import istat.android.freedev.forms.FormState;
/**
 * @author istat
 */
public interface MultiFormHandler {
	public void pullFrom(String url);

	public void pushTo(String url);

	public void clear();

	public List<Form> getForms();

	public FormCheckSummary checkUp(boolean breakIfError);

	public FormState checkUp(int index);

	public void fillFromFormEntities(Form... forms);

	public void flowIntoFormEntities(Form... forms);

	public void clear(int index);
}
