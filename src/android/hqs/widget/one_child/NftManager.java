package android.hqs.widget.one_child;

import android.app.Notification;
import android.content.Context;
import android.hqs.basic.BasicContext;

/**
 * 通知显示管理
 * @ClassName NftManager
 * @author 胡青松
 * @date 2016年9月23日 上午9:29:09
 * @version 1.0
 * @Copyright © 2016 广东维沃移动通信有限公司. All rights reserved.
 */
public class NftManager extends BasicContext{
	
	/** 如果用户不设置id，那么其作为本通知的一个默认id */
	private final int id = 4531;

	private int tickerText; 
	private int smallIcon; 
	private int contentTitle; 
	private int contentText; 

	/** @Description 通知显示管理 */
	private static NftManager mNftManager = null;
	
	/**
	 * 只創建一個實例，然后调用{@link #initResIds(int, int, int, int)}初始化资源id，
	 * 这样做是为了避免没有R.java文件报错的困扰。
	 * @return NftManager
	 */
	public synchronized static NftManager getInstance(final Context context) {
		if (mNftManager == null) {
			mNftManager = new NftManager(context);
		}
		return mNftManager;
	}

	private NftManager(final Context context) {
		super(context);
	}
	
	/** 这里初始化所以资源id */
	public void initResIds(int tickerText, int smallIcon, int contentTitle, int contentText){
		this.tickerText = tickerText;
		this.smallIcon = smallIcon;
		this.contentTitle = contentTitle;
		this.contentText = contentText;
	}

	/**
	 * @Description 取消状态栏信息
	 */
	public synchronized void cancelNft() {
		cancelNft(id);
	}

	/**
	 * @Description 取消状态栏的信息提醒
	 * @param id
	 *            状态栏信息ID
	 */
	public void cancelNft(int id) {
		getNotificationManager().cancel(id);
	}

	/** TODO 創建并顯示或更新通知 */
	public synchronized void sendNft() {
		Notification.Builder mBuilder = new Notification.Builder(getContext());
		mBuilder.setTicker(getString(tickerText))
		// 设置状态栏（如果没有设置大图，也将用到下拉菜单栏）图标
		.setSmallIcon(smallIcon)
		.setWhen(System.currentTimeMillis())
		.setContentTitle(getString(contentTitle))
		.setContentText(getString(contentText))
		.setPriority(1000)
		.setAutoCancel(true);
		
		// 创建通知对象（注意创建对象必须在builder初始化设置完成再通过builder.build()创建，不然通知将为空白，上面设置的数据将不显示。）
		Notification notification = mBuilder.build();
		// 设置通知特性
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		getNotificationManager().notify(id, notification);
	}
	
}
