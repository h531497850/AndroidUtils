package android.hqs.util;

import java.io.Serializable;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class HandlerUtil {

	public static void sendMsg(Handler h, int what) {
		h.sendMessage(h.obtainMessage(what));
	}
	
	public static void sendMsg(Handler h, int what, int arg1) {
		Message msg = h.obtainMessage(what);
		msg.arg1 = arg1;
		h.sendMessage(msg);
	}
	
	public static void sendMsg(Handler h, int what, int arg1, int arg2) {
		//Message msg = h.obtainMessage(what, arg1, arg2);
		//msg.sendToTarget();
		h.sendMessage(Message.obtain(h, what, arg1, arg2));
		
	}
	public static void sendMsg(Handler h, int what, Object obj) {
		h.sendMessage(Message.obtain(h, what, obj));
	}
	
	/**
	 * @param h
	 * @param what
	 * @param data 封装到bundle内，取出的时候可以使用msg.getData()
	 */
	public static void sendMsg(Handler h, int what, Serializable data) {
		Message msg = h.obtainMessage(what);
		Bundle bundle = new Bundle();
		bundle.putSerializable("data", data);
		msg.setData(bundle);
		h.sendMessage(msg);
	}
	
	public static void sendMsg(Handler h, int what, int arg1, Object obj) {
		Message msg = h.obtainMessage(what, obj);
		msg.arg1 = arg1;
		h.sendMessage(msg);
	}
	
	public static void sendMsg(Handler h, int what, int arg1, int arg2, Object obj) {
		h.sendMessage(Message.obtain(h, what, arg1, arg2, obj));
	}
	
	public static void sendMsgDelayed(Handler h, int what, int arg1, int time) {
		Message msg = h.obtainMessage(what);
		msg.arg1 = arg1;
		h.sendMessageDelayed(msg, time);
	}
	
}
