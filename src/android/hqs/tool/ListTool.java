package android.hqs.tool;

import java.util.List;

import android.widget.AdapterView;

public class ListTool {
	
	public static <T> boolean isListEmpty(List<T> list){
		if (list == null || list.size() == 0) {
			return false;
		}
		return true;
	}

	/**
	 * 当前索引是否有数据
	 * @param index
	 * @param list
	 * @return
	 */
	public static <T> boolean hasDataInListByIndex(int index, List<T> list){
		if (list == null || list.size() == 0) {
			return false;
		}
		if (index >= 0 && index < list.size()) {
			return true;	
		}
		return false;
	}
	
	/**
	 * 当前索引是否在ListView显示出来
	 * @param index
	 * @param view
	 * @return
	 */
	public static boolean isListShowTheIndex(int index, AdapterView<?> view){
		if (view == null) {
			return false;
		}
		if (index >= view.getFirstVisiblePosition() && index <= view.getLastVisiblePosition()) {
			return true;
		}
		return false;
	}

}
