package android.istat.base.forms;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;

class ViewUtil {
	private ViewUtil() {

	}

	public static List<View> getAllChildViews(View v) {
		if (!(v instanceof ViewGroup)) {
			ArrayList<View> viewArrayList = new ArrayList<View>();
			viewArrayList.add(v);
			return viewArrayList;
		}

		ArrayList<View> result = new ArrayList<View>();

		ViewGroup viewGroup = (ViewGroup) v;
		for (int i = 0; i < viewGroup.getChildCount(); i++) {

			View child = viewGroup.getChildAt(i);

			ArrayList<View> viewArrayList = new ArrayList<View>();
			viewArrayList.add(v);
			viewArrayList.addAll(getAllChildViews(child));

			result.addAll(viewArrayList);
		}
		return result;
	}

	public static List<View> getAllChildViews(View v, boolean ignoreViewGroup) {
		if (!(v instanceof ViewGroup)) {
			ArrayList<View> viewArrayList = new ArrayList<View>();
			viewArrayList.add(v);
			return viewArrayList;
		}

		ArrayList<View> result = new ArrayList<View>();

		ViewGroup viewGroup = (ViewGroup) v;
		for (int i = 0; i < viewGroup.getChildCount(); i++) {

			View child = viewGroup.getChildAt(i);

			ArrayList<View> viewArrayList = new ArrayList<View>();
			if (!ignoreViewGroup) {
				viewArrayList.add(v);
			}
			viewArrayList.addAll(getAllChildViews(child));

			result.addAll(viewArrayList);
		}
		return result;
	}

	public static List<View> getDirectChildViews(ViewGroup v) {
		// TODO Auto-generated method stub
		return getAllChildViews(v, true);
	}

	public static List<View> getAllChildViews(ViewGroup v) {
		// TODO Auto-generated method stub
		return getAllChildViews(v, false);
	}

}
