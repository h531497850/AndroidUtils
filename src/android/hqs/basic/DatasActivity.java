package android.hqs.basic;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.XmlResourceParser;
import android.graphics.Movie;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * 主要生命周期{@link #onCreate(Bundle)}、@{@link #onRestoreInstanceState(Bundle)}、{@link #onStart()}、
 * {@link #onRestart()}、{@link #onResume()}、@{@link #onSaveInstanceState(Bundle)}、{@link #onPause()}、
 * {@link #onStop()}、{@link #onDestroy()}。<p>
 * 
 * 在{@link #onCreate(Bundle)}中主要做4件事：</br>
 * 在初始化UI之前可能会修改布局样式，如调用getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
 * 1、（为全屏，但系统状态栏依然显示在最上层）</br>
 * 2、initView：初始化UI界面，如果有必要先初始化相关布局再加载界面，LayoutParams影响UI的显示样式等；</br>
 * 3、initComponents：初始化各种主键，并注册（register）相关监听器；</br>
 * 4、initData：初始化数据，主要是初始化Presenter，而在Presenter内主要有以下几种数据获取方式：</br>
 * 		<ul>
 * 		<li>intent</li>
 * 		<li>网络数据</li>
 * 		<li>服务传输过来的信息。</li>
 * 		</ul>
 * 		数据初始化完成之后要注意界面的刷新。
 * 
 * <strong>请慎用该Activity，该Activity被系统杀死后如果不保存数据，
 * 那么下次进入就算重新创建了Activity也会出现数据为null的情况。 所以最好建立一个数据库，如{@link #SQLiteOpenHelper}
 * ，也不建议使用{@link #SharedPreferences}保存数据，因为它在5秒之内的数据如果断电不会保存成功。</strong>
 * 
 * @author hqs2063594
 */
public abstract class DatasActivity extends Activity {
	
	/** 类名，用于生成打印日志的标签，不能为空 */
	private final String Tag;
	/** 获取实例类名 */
	public final String getClsName() {
		return Tag;
	}
	
	/**
	 * 初始化Activity，生成实例类名，可作为打印日志的标签，<b>注意：这时上下文(context)还没实现</b>，
	 * 它是在系统调用{@link #onCreate(Bundle)}之后实现的。所以子类实现该构造时不要在构造方法里初始化一些需要上下文的实例。
	 */
	public DatasActivity() {
		this.Tag = getClass().getSimpleName();
	}
	
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		info("onRestoreInstanceState()");
		super.onRestoreInstanceState(savedInstanceState);
	}
	
	/**
	 * 当某个activity变得“容易”被系统销毁时，该activity的onSaveInstanceState就会被执行，
	 * 除非该activity是被用户主动销毁的，例如当用户按BACK键的时候。<p>
	 * 注意上面的双引号，何为“容易”？言下之意就是该activity还没有被销毁，而仅仅是一种可能性。
	 * onSaveInstanceState方法会在什么时候被执行，有这么几种情况：
	 * <ul>
     * <li>当用户按下HOME键时。</li>
     * <li>长按HOME键，选择运行其他的程序时。</li>
     * <li>按下电源按键（关闭屏幕显示）时。</li>
     * <li>从activity A中启动一个新的activity时。</li>
     * <li>屏幕方向切换时，例如从竖屏切换到横屏时。</li>
     * </ul>
	 * 注意的是，该方法和@{@link #onRestoreInstanceState()}方法“不一定”是成对的被调用的。
	 */
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		info("onSaveInstanceState()");
		super.onSaveInstanceState(savedInstanceState);
	}
	
	/**
	 * 横竖屏切换时会调用该方法
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		
		// 判断屏幕方向
		switch (newConfig.orientation) {
		case Configuration.ORIENTATION_LANDSCAPE: 	// 横屏
			
			break;
		case Configuration.ORIENTATION_PORTRAIT: 	// 竖屏
			
			break;
		default:
			break;
		}
		
		// 检测实体键盘的状态：推出或者合上
		switch (newConfig.hardKeyboardHidden) {
		case Configuration.HARDKEYBOARDHIDDEN_NO:	// 推出
			
			break;
		case Configuration.HARDKEYBOARDHIDDEN_YES:	// 合上
			
			break;
		default:
			break;
		}
	}
	
	/*private int getOrientation() {
		Display display = getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		int height = display.getHeight();
		if (width > height) {
			return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
		} else {
			return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
		}
	}*/
	
	// ========================================================================================================
	// ======================================== TODO 下面是公布给子类的方法 ======================================
	// ========================================================================================================
	protected final XmlResourceParser getAnimation(int id) {
		return getResources().getAnimation(id);
	}
	
	protected final XmlResourceParser getLayout(int id) {
		return getResources().getLayout(id);
	}
	
	protected final XmlResourceParser getXml(int id) {
		return getResources().getXml(id);
	}
	
	/**
	 * 根据特定资源Id返回一个布尔值，可以使用任何完整res值，如果res值不为0，返回该真实值。
	 * @param id 通过aapt工具生成的资源标识符。这个整数包含编码包、类型和资源入口，值0是一个无效的标识符。
	 * @return 
	 */
	protected final boolean getBoolean(int id) {
		return getResources().getBoolean(id);
	}
	
	/**
	 * TODO 注意sdk6.0已经有方法{@link #getColor(int)}
	 */
	protected final int getColor(int id) {
		return getResources().getColor(id);
	}
	
	/**
	 * 返回一个与特定资源标识相关联的整数。
	 * @param id 通过aapt工具生成的资源标识符。这个整数包含编码包、类型和资源入口，值0是一个无效的标识符。
	 * @return
	 */
	protected final int getInteger(int id) {
		return getResources().getInteger(id);
	}
	
	/**
	 * 根据特定的资源ID获取尺寸，单位转换基于与资源相关的当前的DisplayMetrics。
	 * @param id 通过aapt工具生成的资源标识符。这个整数包含编码包、类型和资源入口，值0是一个无效的标识符。
	 * @return
	 */
	protected final float getDimension(int id) {
		return getResources().getDimension(id);
	}
	
	protected final DisplayMetrics getDisplayMetrics() {
		return getResources().getDisplayMetrics();
	}
	
	/**
	 * TODO 注意sdk6.0已经有{@link #getDrawable(int)}<br>
	 * 获取资源ID相关联的Drawable对象，对象的各种类型取决于基本资源--例如，纯色，PNG图像，图像分级等，实现细节被绘图API隐藏。
	 * @param id 通过aapt工具生成的资源标识符。这个整数包含编码包、类型和资源入口，值0是一个无效的标识符。
	 * @return
	 */
	protected final Drawable getDrawable(int id) {
		return getResources().getDrawable(id);
	}
	
	/**
	 * 返回一个与特定资源标识相关联的Movie对象。
	 * @param id
	 * @return
	 */
	protected final Movie getMovie(int id) {
		return getResources().getMovie(id);
	}
	
	/**
	 * 返回给定资源标识符的输入名称。
	 * @param id
	 * @return
	 */
	protected final String getResourceEntryName(int id) {
		return getResources().getResourceEntryName(id);
	}
	
	/**
	 * 返回给定资源标识符的全名。这个名字是一个字符串的形式“package:type/entry”。<br>
	 * {@link #getResourcePackageName()}，{@link #getResourceTypeName()}，{@link #getResourceEntryName()}
	 * @param id
	 * @return
	 */
	protected final String getResourceName(int id) {
		return getResources().getResourceName(id);
	}
	
	protected final String getResourcePackageName(int id) {
		return getResources().getResourcePackageName(id);
	}
	
	protected final String getResourceTypeName(int id) {
		return getResources().getResourceTypeName(id);
	}

	// ========================================================================================================
	// ==================================== TODO 下面是打印日志的方法 ============================================
	// ========================================================================================================
	private boolean DEBUG = false;
	protected final void setDebug(boolean debug) {
		DEBUG = debug;
	}
	
	protected final void debug(Object obj) {
		if(DEBUG) Log.d(Tag, String.valueOf(obj));
	}
	/**
	 * 蓝色，调试信息
	 * @param methodName 方法名
	 * @param obj 要打印的消息
	 */
	protected final void debug(String methodName, Object obj) {
		if(DEBUG) Log.d(Tag, methodName + " --> " + String.valueOf(obj));
	}
	protected final void debug(String methodName, Throwable tr) {
		if(DEBUG) Log.d(Tag, methodName, tr);
	}
	
	protected final void info(Object obj) {
		if(DEBUG) Log.i(Tag, String.valueOf(obj));
	}
	/**
	 * 绿色，正常信息
	 * @param methodName 方法名
	 * @param obj 要打印的消息
	 */
	protected final void info(String methodName, Object obj) {
		if(DEBUG) Log.i(Tag, methodName + " --> " + String.valueOf(obj));
	}
	protected final void info(String methodName, Throwable tr) {
		if(DEBUG) Log.i(Tag, methodName, tr);
	}
	
	protected final void verbose(Object obj) {
		if(DEBUG) Log.v(Tag, String.valueOf(obj));
	}
	/**
	 *  黑色，冗长信息
	 * @param methodName 方法名
	 * @param obj 要打印的消息
	 */
	protected final void verbose(String methodName, Object obj) {
		if(DEBUG) Log.v(Tag, methodName + " --> " + String.valueOf(obj));
	}
	protected final void verbose(String methodName, Throwable tr) {
		if(DEBUG) Log.v(Tag, methodName, tr);
	}
	
	protected final void error(Object obj) {
		if(DEBUG) Log.e(Tag, String.valueOf(obj));
	}
	/**
	 *  红色，错误信息
	 * @param methodName 方法名
	 * @param obj 要打印的消息
	 */
	protected final void error(String methodName, Object obj) {
		if(DEBUG) Log.e(Tag, methodName + " --> " + String.valueOf(obj));
	}
	protected final void error(String methodName, Throwable tr) {
		if(DEBUG) Log.e(Tag, methodName, tr);
	}
	protected final void error(String methodName, Object obj, Throwable tr) {
		if(DEBUG) Log.e(Tag, methodName + " --> " + String.valueOf(obj), tr);
	}
	
	protected final void wtf(Object obj) {
		if(DEBUG) Log.wtf(Tag, String.valueOf(obj));
	}
	/**
	 * 紫色，不应发生的信息
	 * @param methodName 方法名
	 * @param obj 要打印的消息
	 */
	protected final void wtf(String methodName, Object obj) {
		if(DEBUG) Log.wtf(Tag, methodName + " --> " + String.valueOf(obj));
	}
	protected final void wtf(String methodName, Throwable tr) {
		if(DEBUG) Log.wtf(Tag, methodName, tr);
	}
	
	protected void info(String listName, byte[] list){
		if (DEBUG) {
			if (list == null || list.length == 0) {
				return;
			}
			listName = new String(listName + ", " + list.length + "   :");
			for(int i=0; i < list.length; ++i) {
				listName += String.format("%02x ", list[i]) + ",";
			}
			Log.i(Tag, listName);
		}
	}
	protected final void info(String methodName, String listName, byte[] list) {
		if (DEBUG) {
			if (list == null || list.length == 0) {
				return;
			}
			listName = new String(listName + ", " + list.length + "   :");
			for(int i=0; i < list.length; ++i) {
				listName += String.format("%02x ", list[i]) + ",";
			}
			Log.i(Tag, methodName + " --> " + listName);
		}
	}
	protected void info(String listName, int[] list){
		if (DEBUG) {
			if (list == null || list.length == 0) {
				return;
			}
			listName = new String(listName + ", " + list.length + "   :");
			for(int i=0; i < list.length; ++i) {
				listName += String.valueOf(list[i]) + ",";
			}
			Log.i(Tag, listName);
		}
	}
	protected final void info(String methodName, String listName, int[] list) {
		if (DEBUG) {
			if (list == null || list.length == 0) {
				return;
			}
			listName = new String(listName + ", " + list.length + "   :");
			for(int i=0; i < list.length; ++i) {
				listName += String.valueOf(list[i]) + ",";
			}
			Log.i(Tag, methodName + " --> " + listName);
		}
	}
	
}