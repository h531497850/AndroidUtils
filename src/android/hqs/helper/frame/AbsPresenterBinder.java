package android.hqs.helper.frame;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

/**
 * 要绑定服务的UI逻辑处理器
 * @author hqs2063594
 *
 * @param <T> view UI界面与该类的回调接口，为什么不直接将整个View传进来是因为View本身有很多公用的方法，但这里用不到，
 * 			这样能避免造成不必要的干扰。
 */
public abstract class AbsPresenterBinder<T> extends AbsPresenter<T> implements ServiceConnection{
	
	/**
	 * 可以在该线程下做一些相关的耗时操作
	 */
	private HandlerThread mHandlerThread;
	protected final Handler mHandler;
	private Looper mLooper;
	
	/**
	 * 处理消息，并更新UI true 处理玩该消息后，拦截该消息，不再发送出去；false不拦截，其他接口也能收到该消息
	 * @return 
	 */
	public abstract boolean handleMsg(Message msg);
	
	protected abstract Intent getBindIntent();

	public AbsPresenterBinder(Context context, T view){
		this(context, view, false);
	}
	
	public AbsPresenterBinder(Context context, T view, boolean createHandlerThread) {
		super(context, view);
		
		if (createHandlerThread) {
			mHandlerThread = new HandlerThread("SonggeThread");
			mHandlerThread.start();
			mLooper = mHandlerThread.getLooper();
		} else {
			mLooper = Looper.myLooper();
		}
		mHandler = new Handler(mLooper, new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				return handleMsg(msg);
			}
		});
	}
	
}
