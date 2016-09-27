package android.hqs.helper;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

public class MsgerHelper {

	/**
	 * 
	 * @param m 信使
	 * @param what
	 */
	public static void sendMsg(Messenger m, int what) {
		Message msg = Message.obtain();
		msg.what = what;
		try {
			m.send(msg);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	/**
	 * {@link #Activity}绑定{@link #Service}成功后，用服务的{@link #Messenger}（信使）将activity的{@link #Handler}发送到服务，
	 * 以实现服务向activity传输数据的功能。
	 * @param serviceMessenger
	 * @param what 绑定服务成功的msg标识
	 * @param activityMessenger
	 */
	public static void sendMsg(Messenger serviceMessenger, int what, Messenger activityMessenger) {
		Message msg = Message.obtain();
		msg.what = what;
		msg.replyTo = activityMessenger;
		try {
			serviceMessenger.send(msg);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 在Service中绑定启动服务返回值
	 * @return
	 */
	private IBinder initServiceMessenger(){
		Messenger messenger = new Messenger(mServiceHandler);
		return messenger.getBinder();
	}
	/**
	 * 在activity中绑定成功时获取Service中Messenger的引用，并将Activity中的Handler封装发送到Service
	 */
	private final int bind = 1; 
	private ServiceConnection conn = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {}
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mServiceMessenger = new Messenger(service);
			Messenger m2 = new Messenger(mActivityHandler);
			sendMsg(mServiceMessenger, bind, m2);
		}
	};
	
	/**
	 * 在Service中引用activity的Messenger
	 */
	private Messenger mActivityMessenger;
	private Handler mServiceHandler = new Handler(new Handler.Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case bind:
				mActivityMessenger = msg.replyTo;
				return true;

			default:
				break;
			}
			return false;
		}
	});
	
	/**
	 * 在activity中引用Service的Messenger
	 */
	private Messenger mServiceMessenger;
	private Handler mActivityHandler = new Handler(new Handler.Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			return true;
		}
	});
}
