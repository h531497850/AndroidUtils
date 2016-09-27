package android.hqs.helper;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hqs.bean.FileBean;
import android.widget.RemoteViews;

/**
 * 通知工具
 * @author hqs2063594
 *
 */
public class NotifiHelper {

	private Context context;
	private NotificationManager mManager = null;
	
	/** k是id */
	private Map<Integer, Notification> mNotifications;
	
	public NotifiHelper(Context context) {
		this.context =context;
		// 获取系统通知服务
		mManager = (NotificationManager) context.getSystemService(Activity.NOTIFICATION_SERVICE);
		
		mNotifications = new HashMap<Integer, Notification>();
		
	}
	
	public void showNotification(Context context, Class<?> cls){
		// 通知哪一条消息刷新
		
		//error("showNotification");
		
		NotificationManager mManager = (NotificationManager) context.getSystemService(Activity.NOTIFICATION_SERVICE);
		mManager.notify(0, initNotification(context, cls));
	}
	
	private Notification initNotification(Context context, Class<?> cls){
		// 创建通知对象
		Notification n = new Notification();
		// 注意必须得有该项，不然通知将不会弹出
		//n.icon = R.drawable.ic_launcher;
		// 设置滚动文字
		n.tickerText = "SD卡插入";
		// 设置显示时间
		n.when = System.currentTimeMillis();
		// 设置图标
		//n.icon = R.drawable.ic_launcher;
		// 设置通知特性
		n.flags = Notification.FLAG_AUTO_CANCEL;
		// 设置点击通知栏的操作
		if (cls != null) {
			Intent intent = new Intent(context, cls);
			PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);
			n.contentIntent = pIntent;
		}
		// 设置Notification的视图
		n.contentView = initRemoteViews(context);
		
		//("notification init done");
		
		return n;
	}
	
	private Notification initNotificationByBuilder(Context context, Class<?> cls){
		Builder mBuilder = new Builder(context);
		// 创建通知对象
		Notification n = mBuilder.build();
		// 设置图标
		// 注意必须得有该项，不然通知将不会弹出
		//mBuilder.setSmallIcon(R.drawable.ic_launcher, 1000);
		// 设置显示时间
		mBuilder.setWhen(System.currentTimeMillis());
		// 设置滚动文字和Notification的视图
		mBuilder.setTicker("SD卡插入", initRemoteViews(context));
		mBuilder.setPriority(1000);
		// 设置通知特性
		n.flags = Notification.FLAG_AUTO_CANCEL;
		// 设置点击通知栏的操作
		if (cls != null) {
			Intent intent = new Intent(context, cls);
			PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);
			n.contentIntent = pIntent;
		}
		
		//error("notification init done");
		
		return n;
	}
	
	/**
	 * 初始化自定义布局 
	 * @param bean
	 * @param layoutId
	 */
	private RemoteViews initRemoteViews(Context context) {
		RemoteViews views  = new RemoteViews(context.getPackageName(), 22);
		
		//views.setTextViewText(R.id.ns_tv_noti, "您的SD卡是低速卡，为了您有更好的体验，请更好高速卡！");
		
		//Intent start = new Intent(context, cls);
		//views.setOnClickPendingIntent(viewId, intent);
		
		//Intent stop = new Intent(context, cls);
		//views.setOnClickPendingIntent(viewId, intent);
		
		//error("remote views create done.");
		
		return views;
	}
	
	/**
	 * 显示通知
	 * @param bean
	 */
	public void showNotification(FileBean bean) {
		showNotification(bean, -1);
	}
	/**
	 * 显示通知
	 * @param bean
	 * @param layoutId <= 0 时表示无自定义布局
	 */
	public void showNotification(FileBean bean, int layoutId) {
		showNotification(bean, layoutId, null);
	}
	/**
	 * 显示通知
	 * @param bean
	 * @param layoutId <= 0 时表示无自定义布局
	 * @param cls 点击通知栏时要跳转的位置
	 */
	public void showNotification(FileBean bean, int layoutId, Class<?> cls) {
		if (!mNotifications.containsKey(bean.getId())) {
			// 创建通知对象
			Notification n = new Notification();
			// 设置滚动文字
			n.tickerText = bean.getLabel() + "开始下载";
			// 设置显示时间
			n.when = System.currentTimeMillis();
			// 设置图标
			//n.icon = R.drawable.ic_launcher;
			// 设置通知特性
			n.flags = Notification.FLAG_AUTO_CANCEL;
			
			// 设置点击通知栏的操作
			Intent intent;
			if (cls == null) {
				intent = new Intent();
			} else {
				intent = new Intent(context, cls);
			}
			PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);
			n.contentIntent = pIntent;
			
			// 设置Notification的视图
			if (layoutId > 0) {
				n.contentView = initRemoteViews(bean, layoutId);
			}
			// 通知哪一条消息刷新
			mManager.notify(bean.getId(), n);
			mNotifications.put(bean.getId(), n);
		}
	}
	
	public void updateNotification(int id, Object obj){
		Notification n = mNotifications.get(id);
		if (n == null) {
			return;
		}
		//n.contentView.setProgressBar(viewId, max, progress, indeterminate);
		mManager.notify(id, n);
	}
	
	public void cancelNotification(int id){
		mManager.cancel(id);
		mNotifications.remove(id);
	}

	/**
	 * 初始化自定义布局 
	 * @param bean
	 * @param layoutId
	 */
	private RemoteViews initRemoteViews(FileBean bean, int layoutId) {
		RemoteViews views  = new RemoteViews(context.getPackageName(), layoutId);
		
		//Intent start = new Intent(context, cls);
		//views.setOnClickPendingIntent(viewId, intent);
		
		//Intent stop = new Intent(context, cls);
		//views.setOnClickPendingIntent(viewId, intent);
		
		
		return views;
	}

}
