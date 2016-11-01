package android.hqs.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.hqs.tool.LogcatTool;
import android.util.Log;

/**
 * 获取资源文件
 * @author hqs2063594
 *
 */
public class ResUtil {
    private static final String TAG = LogcatTool.makeTag(ResUtil.class);
	
	/**
	 * 通过标识符获取res/drawable文件夹下面的Drawable
	 * @param context
	 * @param resName 要获取的资源文件的名字，不包括拓展名
	 * @return
	 */
	public static Drawable getDrawable(Context context, String resName) {
		int id = context.getResources().getIdentifier(resName, "drawable", context.getPackageName());
		return context.getResources().getDrawable(id);
	}
	
	/**
	 * <strong>注意该方法适用于java(非Android)</strong>不把图片放在res/drawable下，
	 * 而是存放在src某个package中（如：com.drawable.resource）， 资源名是imageName.png，
	 * 那么 String path = "com/drawable/resource/imageName.png";
	 * 
	 * @param context
	 * @param resName
	 *            要获取的资源文件的名字，包括拓展名
	 * @return
	 */
	public static Drawable getDrawable(Context context, String packageName, String resName) {
		String path = packageName.replace(".", "/") + "/" + resName; 
		InputStream is = context.getClassLoader().getResourceAsStream(path); 
		return Drawable.createFromStream(is, "src");
	}
	
	/**
	 * <strong>注意该方法适用于java(非Android)</strong>不把图片放在res/drawable下，
	 * 而是存放在src某个package中（如：com.drawable.resource）， 资源名是imageName.png，
	 * 那么 String path = "com/drawable/resource/imageName.png";
	 * 
	 * @param packageName
	 * @param resName
	 *            要获取的资源文件的名字，包括拓展名
	 * @return
	 */
	public static Drawable getDrawable(String packageName, String resName) {
		String path = packageName.replace(".", "/") + "/" + resName; 
		InputStream is = ResUtil.class.getResourceAsStream(path); 
		return Drawable.createFromStream(is, "src");
	}
	
	public static int getColor(Context context, String resName){
		int id = context.getResources().getIdentifier(resName, "color", context.getPackageName());
		return context.getResources().getColor(id);
	}
	
	/**
	 * 因为最终是通过流文件来进行properties文件读取的，所以很自然，我们想到要将文件放入到assets文件夹或者raw文件夹中了。
	 * 例如，我们这里有一个文件——>test.properties，如果放入了assets文件夹中，可以如下打开。<p>
	 * 
	 * 假如test.properties内有内容：xmppHost=192.168.0.100，那么可以通过{@link Properties#getProperty(String, String)}
	 * 来获取xmppHost的值，getProperty("xmppHost", "127.0.0.1")。
	 * 
	 * @param context
	 * @param resName 要获取的资源文件的名字，包括拓展名
	 */
	public static Properties loadProperty(Context context, String resName){
		Properties pro = new Properties();
		try {
			InputStream is = context.getAssets().open(resName);
			pro.load(is);
		} catch (IOException e) {
			Log.e(TAG, "loadProperty", e);
		}
		return pro; 
	}
	
	/**
	 * 因为最终是通过流文件来进行properties文件读取的，所以很自然，我们想到要将文件放入到assets文件夹或者raw文件夹中了。
	 * 例如，我们这里有一个文件——>test.properties，如果放入到raw文件夹中，可以通过如下方式打开<p>
	 * 
	 * 假如test.properties内有内容：xmppHost=192.168.0.100，那么可以通过{@link Properties#getProperty(String, String)}
	 * 来获取xmppHost的值，getProperty("xmppHost", "127.0.0.1")。
	 * 
	 * @param context
	 * @param resId 要获取的资源文件的id如：R.raw.test。
	 */
	public static Properties loadProperty(Context context, int resId){
		Properties pro = new Properties();
		try {
			InputStream is = context.getResources().openRawResource(resId);
			pro.load(is);
		} catch (IOException e) {
			Log.e(TAG, "loadProperty", e);
		}
		return pro; 
	}
	
	/**
	 * <strong>注意该方法适用于java(非Android)</strong>没有上下文环境(context)，而是像java中标准的方法加载properties文件。<p>
	 * 
	 * 假如test.properties内有内容：xmppHost=192.168.0.100，那么可以通过{@link Properties#getProperty(String, String)}
	 * 来获取xmppHost的值，getProperty("xmppHost", "127.0.0.1")。
	 * 
	 * @param clazz 该加载方法所在类的类名（如：ResourceUtil.class）
	 * @param resName 要获取的资源文件的名字，包括拓展名，所存放的路径与加载方法所在类（如：ResourceUtil.java）为同一包中。
	 * @return
	 */
	public static Properties loadProperty(String resName){
		Properties pro = new Properties();
		try {
			pro.load(ResUtil.class.getResourceAsStream(resName));
		} catch (IOException e) {
			Log.e(TAG, "loadProperty", e);
		}
		return pro; 
	}
	
	/**
	 * 在android中，当我们打包生成apk后，将apk放入到真正的手机上时程序包中的资源文件编译后，是会丢失的！
	 * android中的资源文件是只能存放在assets或者res的子目录里面的，将文件放入到assets、raw文件夹里，而在传入路径里面填入文件绝对路径。
	 * <strong>注意：其中工程路径的根路径为"/"</strong>。<p>
	 * 
	 * 假如test.properties内有内容：xmppHost=192.168.0.100，那么可以通过{@link Properties#getProperty(String, String)}
	 * 来获取xmppHost的值，getProperty("xmppHost", "127.0.0.1")。
	 * 
	 * @param packageName 包名，如：/assets/..
	 * @param resName 要获取的资源文件的名字，不包括拓展名
	 * @return
	 */
	public static Properties loadProperty(String packageName, String resName){
		Properties pro = new Properties();
		try {
			pro.load(ResUtil.class.getResourceAsStream(packageName + resName + ".properties"));
		} catch (IOException e) {
			Log.e(TAG, "loadProperty", e);
		}
		return pro; 
	}
	
}
