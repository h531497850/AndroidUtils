package android.hqs.component4.acitvity;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;

public abstract class EventActivity extends BaseActivity implements OnClickListener, OnLongClickListener, OnTouchListener {
	
	/**
	 * 通过该方法获取各itemView内的各种小组件如：TextView，ImageView，ImageButton等，并注册单击，长按，触摸监听
	 * @param comId 
	 * @return 你想要的View组件
	 */
	@SuppressWarnings("unchecked")
	public <T extends View> T getComponent(int comId) {
		View Component = getWindow().getDecorView().findViewById(comId);
		Component.setOnClickListener(this);
		Component.setOnLongClickListener(this);
		Component.setOnTouchListener(this);
		return (T) Component;
	}

}
