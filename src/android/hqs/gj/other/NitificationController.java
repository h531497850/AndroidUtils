package android.hqs.gj.other;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.util.SparseArray;

/**
 * @ClassName NitificationControler
 * @Description
 * @author 胡青松
 * @date 2016年3月23日 上午9:29:09
 * @version 1.0
 * @Copyright © 2016 广东维沃移动通信有限公司. All rights reserved.
 */
public class NitificationController {
	
	private SparseArray<NitificationDataBean> nitificationDatas = new SparseArray<NitificationDataBean>(5);
	
	private class NitificationDataBean {
		public int smallIconId;
		public String tickerText;
		public String contentTitle;
		public String contentText;
	}
	
	/**
	 * 外部没有设置
	 */
	private final int defaultId = 4531;
	//private long[] mVibrate = { 0, 100, 200, 300 };

	private Context mContext;

	private static NitificationController mLocalNitificationManager = null;

	/**
	 * @Description TODO
	 * @param context
	 * @return LocalNitificationManager
	 */
	public synchronized static NitificationController getInstance(final Context context) {
		if (mLocalNitificationManager == null) {
			mLocalNitificationManager = new NitificationController(context);
		}

		return mLocalNitificationManager;
	}

	/**
	 * @Title LocalNitificationManager
	 * @Description TODO
	 * @param context
	 */
	private NitificationController(final Context context) {
		this.mContext = context;
	}

	/**
	 * @Description 取消状态栏信息
	 */
	public synchronized void cancelNitification() {
		NotificationManager mgr = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		mgr.cancel(defaultId);
	}

	/**
	 * @Description 取消状态栏的信息提醒
	 * @param id
	 *            状态栏信息ID
	 */
	public void cancelNitification(int id) {
		NotificationManager mgr = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		mgr.cancel(id);
	}
	
	public synchronized void initNotification(int smallIconId, String tickerText,
			String contentTitle, String contentText) {
		initNotification(defaultId, smallIconId, tickerText, contentTitle, contentText);
	}
	public synchronized void initNotification(int id, int smallIconId, String tickerText,
			String contentTitle, String contentText) {
		NitificationDataBean bean = nitificationDatas.get(id);
		if (bean == null) {
			bean = new NitificationDataBean();
			bean.smallIconId = smallIconId;
			bean.tickerText = tickerText;
			bean.contentTitle = contentTitle;
			bean.contentText = contentText;
			nitificationDatas.put(id, bean);
		}
	}

	/**
	 * @Description TODO
	 * @param app
	 * @param info
	 * @param pendingIntent1
	 * @param pendingIntent2
	 * @return void
	 */
	public synchronized void sendNotification(int id) {
		NotificationManager mgr = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification.Builder mBuilder = new Notification.Builder(mContext);
		NitificationDataBean bean = nitificationDatas.get(id);
		
		// 自定义布局
		//mBuilder.setContent(views);
		
		mBuilder.setTicker(bean.tickerText)
		// 设置状态栏（如果没有设置大图，也将用到下拉菜单栏）图标
		.setSmallIcon(bean.smallIconId)
		.setWhen(System.currentTimeMillis())
		.setContentTitle(bean.contentTitle)
		.setContentText(bean.contentText)
		.setPriority(1000)/*
		.setAutoCancel(true)*/;
		// 创建通知对象（注意创建对象必须在builder初始化设置完成再通过builder.build()创建，不然通知将为空白，上面设置的数据将不显示。）
		Notification notification = mBuilder.build();
		// 设置通知特性
		//notification.flags = Notification.FLAG_NO_CLEAR;
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		mgr.notify(id, notification);
	}
	
}
