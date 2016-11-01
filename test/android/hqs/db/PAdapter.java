package android.hqs.db;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * 继承{@link ViewGourp}的组件来显示各子视图的基本适配器类
 * @author hqs2063594
 * 
 */
public class PAdapter extends PagerAdapter {

	private List<View> pages = new ArrayList<View>();

	public PAdapter(List<View> pages) {
		if (pages == null) {
			throw new NullPointerException("pages can not be null!");
		}
		this.pages = pages;
	}
	
	@Override
	public int getCount() {
		return pages.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	/**
	 * 实例化各页，用{@link #isViewFromObject(View, Object)}与container内的数据作比较，如果相同
	 */
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		/*int containerCount = container.getChildCount();
		if (getCount() == containerCount) {
			return container.getChildAt(position);
		}
		View page = pages.get(position);
		container.addView(page);
		return page;*/
		
		View page = pages.get(position);
		ViewGroup parent = (ViewGroup) page.getParent();
		if (parent != null) {
			parent.removeAllViews();
		}
		container.addView(page);
		return page;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView( pages.get(position));
	}

}
