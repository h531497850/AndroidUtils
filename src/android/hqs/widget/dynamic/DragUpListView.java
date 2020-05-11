package android.hqs.widget.dynamic;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

/**
 * 向上拖动提示正在加载数据，数据加载完成后移除布局
 * @author hqs2063594
 *
 */
public class DragUpListView extends ListView implements OnScrollListener {

	/**
	 * 底部正在加载布局
	 */
	private View footerView;
	
	/**
	 * 当前最后一个可见的Item
	 */
	private int lastVisibleItem;
	/**
	 * 总数
	 */
	private int totalItemCount;
	
	private boolean isLoading;
	
	
	private ILoadListener mLoadListener;
	/**
	 * 设置加载更多数据的回调接口
	 * @param listener
	 */
	public void setLoadListener(ILoadListener listener) {
		this.mLoadListener = listener;
	}
	/**
	 * 加载更多数据的回调接口
	 * @author hqs2063594
	 *
	 */
	public interface ILoadListener{
		void onLoad();
	}
	
	public DragUpListView(Context context) {
		this(context, null);
	}
	public DragUpListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	public DragUpListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	private void init() {
		setOnScrollListener(this);
	}
	
	/**
	 * 添加底部加载提示布局到ListView
	 * @param layoutId
	 */
	public void initFootView(int layoutId) {
		LayoutInflater inflater = LayoutInflater.from(getContext());
		footerView = inflater.inflate(layoutId, this, false);
		footerView.setVisibility(GONE);
		addFooterView(footerView);
	}
	
	/**
	 * 数据加载完成
	 */
	public void loadComplete(){
		isLoading = false;
		if (footerView != null) {
			footerView.setVisibility(GONE);
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (totalItemCount == lastVisibleItem && scrollState == SCROLL_STATE_IDLE) {
			if (!isLoading) {
				isLoading = true;
				if (footerView != null) {
					footerView.setVisibility(VISIBLE);
				}
				if (mLoadListener != null) {
					mLoadListener.onLoad();
				}
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		this.lastVisibleItem = firstVisibleItem + visibleItemCount;
		this.totalItemCount = totalItemCount;

	}

}
