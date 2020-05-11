package android.hqs.gj.tool;

import java.util.List;
import java.util.Map;

import android.widget.AdapterView;

/**
 * 由于List的{@link List#size()}返回int，所以它最多能放{@link Integer#MAX_VALUE}（即(2^31)-1）个元素。
 * ((旧容量 * 3) / 2) + 1 
 * 注：这点与C#语言是不同的，C#当中的算法很简单，是翻倍。
 * 
 * @author 胡青松
 */
public class DataTool {
	private static final String TAG = LogTool.makeTag("DataTool");
	
	public static <T> boolean isEmpty(List<T> list){
		if (list == null || list.size() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 当前索引是否有数据
	 * @param index
	 * @param list
	 * @return
	 */
	public static <T> boolean hasDataAtIndex(int index, List<T> list){
		if (list == null || list.size() == 0) {
			return false;
		}
		if (index >= 0 && index < list.size()) {
			return true;	
		}
		return false;
	}
	
	public static boolean isEmpty(Map<?,?> params){
		if (params == null || params.size() == 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * 当前索引是否在AdapterView显示出来
	 * @param index
	 * @param view
	 * @return
	 */
	public static boolean isShowAtIndex(int index, AdapterView<?> view){
		if (view == null) {
			return false;
		}
		if (index >= view.getFirstVisiblePosition() && index <= view.getLastVisiblePosition()) {
			return true;
		}
		return false;
	}
	
}	
