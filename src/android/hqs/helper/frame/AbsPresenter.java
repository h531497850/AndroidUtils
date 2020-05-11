package android.hqs.helper.frame;

import android.app.Activity;
import android.content.Context;
import android.hqs.basic.BasicContext;

/**
 * 不绑定服务的UI逻辑处理器
 * @author hqs2063594
 *
 * @param <T> view
 */
public abstract class AbsPresenter<T> extends BasicContext {
	
	/**
	 * UI视图，通过该接口调用界面的各种方法
	 */
	protected T mView;
	
	public AbsPresenter(Context context, T view){
		super(context);
		mView = view;
	}
	
	/**
	 *  界面（activity）恢复（即调用{@link Activity#onResume}）时，如果<b>子类重写</b>了该方法，请同时调用
	 */
	public void onResume(){}
	/**
	 * 界面（activity）中断（即调用{@link Activity#onPause}）时，如果<b>子类重写</b>了该方法，请同时调用
	 */
	public void onPause(){}
	/**
	 * 界面（activity）被摧毁（即调用{@link Activity#onDestroy}）时，如果<b>子类重写</b>了该方法，请同时调用
	 */
	public void onDestroy(){}
	
}
