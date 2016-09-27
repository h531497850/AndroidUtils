package android.hqs.widget.menu;

import android.hqs.helper.AnimHelper;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

/**
 * 创建菜单进出动画的Factory类。
 * 
 * @author Siyamed SINIR
 *
 */
public class SatelliteAnimCreator {
    
    public static Animation createItemInAnimation(int index, long expandDuration, int x, int y){
        RotateAnimation rotate = new RotateAnimation(720, 0, 
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        
		rotate.setInterpolator(new AccelerateInterpolator(1.2f));
		// 也可以使用xml的动画加载器
		// 这样配置：
		// <accelerateInterpolator xmlns:android="http://schemas.android.com/apk/res/android" android:factor="1.2"/>
        // rotate.setInterpolator(context, R.anim.interpolator);
        rotate.setDuration(expandDuration);
        
        TranslateAnimation translate = new TranslateAnimation(x, 0, y, 0);
        long delay = 250;
        if(expandDuration <= 250){
            delay = expandDuration / 3;
        }         
        long duration = 400;
        if((expandDuration-delay) > duration){
        	duration = expandDuration-delay; 
        }
        translate.setDuration(duration);
        translate.setStartOffset(delay);
        translate.setInterpolator(new AnticipateInterpolator(3.0f));
        // 也可以使用xml的动画加载器
     	// 这样配置：
        // <anticipateInterpolator xmlns:android="http://schemas.android.com/apk/res/android" android:tension="3.0"/>
        // translate.setInterpolator(context, R.anim.interpolator);
        
        AlphaAnimation alpha = new AlphaAnimation(1f, 0f);
        long alphaDuration = 10;
        if(expandDuration < 10){
        	alphaDuration = expandDuration / 10;
        }
        alpha.setDuration(alphaDuration);
        alpha.setStartOffset((delay + duration) - alphaDuration);
        
        AnimationSet set = new AnimationSet(false);
        set.setFillAfter(false);
        set.setFillBefore(true);
        set.setFillEnabled(true);
        
        set.addAnimation(alpha);
        set.addAnimation(rotate);
        set.addAnimation(translate);
        
        set.setStartOffset(30*index);
        set.start();
        set.startNow();
        return set;
    }
    
    public static Animation createItemOutAnimation(int index, long expandDuration, int x, int y){
    	
        AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
        long alphaDuration = 60;
        if(expandDuration < 60){
        	alphaDuration = expandDuration / 4;
        }
        alphaAnimation.setDuration(alphaDuration);
        alphaAnimation.setStartOffset(0);

        TranslateAnimation translate = new TranslateAnimation(0, x, 0, y);
        translate.setStartOffset(0);
        translate.setDuration(expandDuration);
        translate.setInterpolator(new OvershootInterpolator(3.0f));
        // 也可以使用xml的动画加载器
     	// 这样配置：
        // <overshootInterpolator xmlns:android="http://schemas.android.com/apk/res/android" android:tension="3.0"/>
        // translate.setInterpolator(context, R.anim.interpolator);
        
        RotateAnimation rotate = new RotateAnimation(0f, 360f, 
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setInterpolator(new DecelerateInterpolator(1.0f));
        // 也可以使用xml的动画加载器
     	// 这样配置：
        // <decelerateInterpolator xmlns:android="http://schemas.android.com/apk/res/android" android:factor="1.0"/>
        // translate.setInterpolator(context, R.anim.interpolator);
        long duration = 100;
        if(expandDuration <= 150){
            duration = expandDuration / 3;
        }
        rotate.setDuration(expandDuration-duration);
        rotate.setStartOffset(duration);        
        
        AnimationSet set = new AnimationSet(false);
        set.setFillAfter(false);
        set.setFillBefore(true);
        set.setFillEnabled(true);
                
        //animationSet.addAnimation(alphaAnimation);
        //animationSet.addAnimation(rotate);
        set.addAnimation(translate);
        set.setStartOffset(30*index);
        return set;
    }
    
    public static Animation createMainButtonAnimation(){
    	Animation anim = AnimHelper.rotate(0, -135, 300);
    	anim.setInterpolator(new DecelerateInterpolator());
    	return anim;
    }
    
    public static Animation createMainButtonInverseAnimation(){
    	Animation anim = AnimHelper.rotate(-135, 0, 300);
    	anim.setInterpolator(new DecelerateInterpolator());
    	return anim;
    }
    
    public static Animation createItemClickAnimation(){
    	ScaleAnimation scale = new ScaleAnimation(1, 3, 1, 3, 0.5f, 0.5f);
    	scale.setDuration(400);
    	AlphaAnimation alpha = new AlphaAnimation(1.0f, 0f);
    	alpha.setDuration(150);
    	alpha.setStartOffset(250);
    	AnimationSet set = new AnimationSet(true);
    	set.addAnimation(scale);
    	set.addAnimation(alpha);
    	set.setFillAfter(false);
    	set.setFillEnabled(false);
    	return set;
    }

    
    public static int getTranslateX(float degree, int distance){
        return Double.valueOf(distance * Math.cos(Math.toRadians(degree))).intValue();
    }
    
    public static int getTranslateY(float degree, int distance){
        return Double.valueOf(-1 * distance * Math.sin(Math.toRadians(degree))).intValue();
    }

}
