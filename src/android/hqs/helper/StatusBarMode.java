package android.hqs.helper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * 白底黑字！Android浅色状态栏黑色字体模式
 * @author 胡青松
 */
public class StatusBarMode {

	/**
	 * 设置状态栏图标为深色和魅族特定的文字风格，Flyme4.0以上可以用来判断是否为Flyme用户
	 * @param window 需要设置的窗口
	 * @param dark 是否把状态栏字体及图标颜色设置为深色
	 * @return  boolean 成功执行返回true
	 */
	public static boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
	    boolean result = false;
	    if (window != null) {
	        try {
	            WindowManager.LayoutParams lp = window.getAttributes();
	            Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
	            Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
	            darkFlag.setAccessible(true);
	            meizuFlags.setAccessible(true);
	            int bit = darkFlag.getInt(null);
	            int value = meizuFlags.getInt(lp);
	            if (dark) {
	                value |= bit;
	            } else {
	                value &= ~bit;
	            }
	            meizuFlags.setInt(lp, value);
	            window.setAttributes(lp);
	            result = true;
	        } catch (Exception e) {

	        }
	    }
	    return result;
	}
	
	/**
	 * 设置状态栏字体图标为深色，需要MIUIV6以上
	 * @param window 需要设置的窗口
	 * @param dark 是否把状态栏字体及图标颜色设置为深色
	 * @return  boolean 成功执行返回true
	 *
	 */
	public static boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
	    boolean result = false;
	    if (window != null) {
	        Class<?> clazz = window.getClass();
	        try {
	            int darkModeFlag = 0;
	            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
	            Field  field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
	            darkModeFlag = field.getInt(layoutParams);
	            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
	            if(dark){
	                extraFlagField.invoke(window,darkModeFlag,darkModeFlag);//状态栏透明且黑色字体
	            }else{
	                extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
	            }
	            result=true;
	        }catch (Exception e){

	        }
	    }
	    return result;
	}
	
	public static void transparencyBar(Activity activity){
	    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
	        Window window = activity.getWindow();
	        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
	                | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
	        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
	                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
	                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
	        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
	        window.setStatusBarColor(Color.TRANSPARENT);
	        window.setNavigationBarColor(Color.TRANSPARENT);
	    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
	        Window window =activity.getWindow();
	        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
	                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
	    }
	}
	
	public static void setStatusBarColor(Activity activity,int colorId) {
	    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
	        Window window = activity.getWindow();
//	      window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);    
	        window.setStatusBarColor(activity.getResources().getColor(colorId));
	    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
	        //使用SystemBarTint库使4.4版本状态栏变色，需要先将状态栏设置为透明
	        transparencyBar(activity);
	        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
	        tintManager.setStatusBarTintEnabled(true);
	        tintManager.setStatusBarTintResource(colorId);
	    }
	}


}
