package android.hqs.basic;

import com.vivo.android.util.LogUtil;

import android.app.Activity;
import android.content.res.XmlResourceParser;
import android.graphics.Movie;
import android.os.Bundle;
import android.util.DisplayMetrics;

/**
 * 主要生命周期{@link #onCreate(Bundle)}、{@link #onStart()}、@{@link #onRestart()}、{@link #onResume()}、{@link #onPause()}、
 * {@link #onStop()}、{@link #onDestroy()}。
 * <p>
 * 
 * 在{@link #onCreate(Bundle)}中主要做4件事：</br>
 * 在初始化UI之前可能会修改布局样式，如调用getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
 * 1、（为全屏，但系统状态栏依然显示在最上层）</br>
 * 2、initView：初始化UI界面，如果有必要先初始化相关布局再加载界面，LayoutParams影响UI的显示样式等；</br>
 * 3、initComponents：初始化各种主键，并注册（register）相关监听器；</br>
 * 4、initData：初始化数据，主要是初始化Presenter，而在Presenter内主要有以下几种数据获取方式：</br>
 * <ul>
 * <li>intent</li>
 * <li>网络数据</li>
 * <li>服务传输过来的信息。</li>
 * </ul>
 * 数据初始化完成之后要注意界面的刷新。
 * 
 * @author hqs2063594
 */
public class BasicActivity extends Activity {
	private final String Tag = LogUtil.makeTag(getClass());

	/**
	 * 初始化activity，生成实例类名，可作为打印日志的标签，<b>注意：这时上下文(context)还没实现</b>，
	 * 它是在系统调用{@link #onCreate(Bundle)}之后实现的。所以子类实现该构造时不要在构造方法里初始化一些需要上下文的实例。
	 */
	public BasicActivity() {
	}

	// =================================================================
	// =======TODO These methods are visible to the subclass ===========
	// =================================================================
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
	 * 
	 * @param id
	 *            通过aapt工具生成的资源标识符。这个整数包含编码包、类型和资源入口，值0是一个无效的标识符。
	 * @return
	 */
	protected final boolean getBoolean(int id) {
		return getResources().getBoolean(id);
	}

	/**
	 * 返回一个与特定资源标识相关联的整数。
	 * 
	 * @param id
	 *            通过aapt工具生成的资源标识符。这个整数包含编码包、类型和资源入口，值0是一个无效的标识符。
	 * @return
	 */
	protected final int getInteger(int id) {
		return getResources().getInteger(id);
	}

	/**
	 * 根据特定的资源ID获取尺寸，单位转换基于与资源相关的当前的DisplayMetrics。
	 * 
	 * @param id
	 *            通过aapt工具生成的资源标识符。这个整数包含编码包、类型和资源入口，值0是一个无效的标识符。
	 * @return
	 */
	protected final float getDimension(int id) {
		return getResources().getDimension(id);
	}

	protected final DisplayMetrics getDisplayMetrics() {
		return getResources().getDisplayMetrics();
	}

	/**
	 * 返回一个与特定资源标识相关联的Movie对象。
	 * 
	 * @param id
	 * @return
	 */
	protected final Movie getMovie(int id) {
		return getResources().getMovie(id);
	}

	/**
	 * 返回给定资源标识符的输入名称。
	 * 
	 * @param id
	 * @return
	 */
	protected final String getResourceEntryName(int id) {
		return getResources().getResourceEntryName(id);
	}

	/**
	 * 返回给定资源标识符的全名。这个名字是一个字符串的形式“package:type/entry”。<br>
	 * {@link #getResourcePackageName()}，{@link #getResourceTypeName()}，{@link #getResourceEntryName()}
	 * 
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

	// =================================================================
	// =================== TODO public methods =========================
	// =================================================================
	/** 获取实例类名 */
	public final String getClsName() {
		return getClass().getSimpleName();
	}

	// =================================================================
	// =================== TODO print log methods ======================
	// =================================================================
	/** Blue, debug information */
	protected final void debug(Object obj) {
		LogUtil.debug(Tag, obj);
	}

	protected final void debug(Object obj, Throwable tr) {
		LogUtil.debug(Tag, obj, tr);
	}

	/** Green, normal information */
	protected final void info(Object obj) {
		LogUtil.info(Tag, obj);
	}

	protected final void info(Object obj, Throwable tr) {
		LogUtil.info(Tag, obj, tr);
	}

	/** Black, long message */
	protected final void verbose(Object obj) {
		LogUtil.verbose(Tag, obj);
	}

	protected final void verbose(Object obj, Throwable tr) {
		LogUtil.verbose(Tag, obj, tr);
	}

	/** Red, error message */
	protected final void error(Object obj) {
		LogUtil.error(Tag, obj);
	}

	protected final void error(Object obj, Throwable tr) {
		LogUtil.error(Tag, obj, tr);
	}

}
