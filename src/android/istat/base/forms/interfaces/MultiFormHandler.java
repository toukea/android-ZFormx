package android.istat.base.forms.interfaces;

import java.util.List;

import android.istat.base.forms.Form;
import android.istat.base.forms.FormCheckSummary;
import android.istat.base.forms.FormState;

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
