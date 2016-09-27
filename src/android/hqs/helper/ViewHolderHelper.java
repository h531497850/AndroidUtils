package android.hqs.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.hqs.util.DimenUtil;
import android.net.Uri;
import android.os.Build;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 通用适配器ItemView组件的辅助工具组件类，注意使用该类所有方法SDK版本最低为16。
 * @author hqs2063594
 *
 */
public class ViewHolderHelper {

	private Context mContext;
	/**
	 * 存放小组件的数组
	 */
	private SparseArray<View> mViews;
	private int mPosition;
	/**
	 * 注意该组件初始化后不可修改
	 */
	private View mConvertView;
	
	/**
	 * 注意在适配器内初始化ViewHolder类时不直接调用该构造方法，而是调用共有的静态方法
	 * {@link #getHolder(Context, View, ViewGroup, int, int)}初始化ViewHolder，因为
	 * {@value #mConvertView}可能已经初始化成功可以直接复用
	 * 
	 * @param context
	 * @param parent
	 * @param layoutId
	 * @param position
	 */
	public ViewHolderHelper(Context context, ViewGroup parent, int layoutId, int position){
		mContext = context;
		mPosition = position;
		mViews = new SparseArray<View>();
		/*
		 * 这里传入3个参数
		 * 原因：
		 * 1、2个参数ViewGroup root=null 时layoutId代表的布局里面设置的各种width、height等将毫无意义；
		 * 2、3个参数的attachToRoot如果为false表示返回的view时根据layoutId加载的ItemView，
		 * 	  true将返回parent并且将ItemView直接加载到parent里面。
		 */
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
		mConvertView.setTag(this);
	}
	
	/**
	 * 调用该方法初始化ViewHolder类
	 * @param context
	 * @param convertView
	 * @param parent
	 * @param layoutId
	 * @param position
	 * @return
	 */
	public static ViewHolderHelper getHolder(Context context, View convertView, ViewGroup parent, int layoutId, int position){
		if (convertView == null) {
			return new ViewHolderHelper(context, parent, layoutId, position);
		} else {
			ViewHolderHelper holder = (ViewHolderHelper) convertView.getTag();
			// position 一直在变化
			holder.mPosition = position;
			return holder;
		}
	}
	
	public int getPosition() {
		return mPosition;
	}

	/**
	 * Adapter中的getView方法返回的View即是该View
	 * @return
	 */
	public View getConvertView() {
		return mConvertView;
	}
	
	public Context getContext() {
		return mContext;
	}

	/**
	 * 通过该方法获取各itemView内的各种小组件如：TextView，ImageView，ImageButton等
	 * @param comId 
	 * @return 你想要的View组件
	 */
	@SuppressWarnings("unchecked")
	public <T extends View> T getComponent(int comId) {
		View component = mViews.get(comId);
		if (component == null) {
			component = mConvertView.findViewById(comId);
			// 将各种小组件存放到map中，下次直接复用
			mViews.put(comId, component);
		}
		return (T) component;
	}
	
	public void releaseMemory(){
		if (mViews != null) {
			mViews.clear();
			mViews = null;
		}
	}

	/**
	 * 获取资源ID相关联的Drawable对象，对象的各种类型取决于基本资源--例如，纯色，PNG图像，图像分级等，实现细节被绘图API隐藏。
	 * @param id 通过aapt工具生成的资源标识符。这个整数包含编码包、类型和资源入口，值0是一个无效的标识符。
	 * @return
	 */
	public final Drawable getDrawable(int id) {
		return mContext.getResources().getDrawable(id);
	}
	
	public final String getString(int id) {
		return mContext.getString(id);
	}
	
	public final int getColor(int id) {
		return mContext.getResources().getColor(id);
	}
	
	public final int getDimension(int id) {
		return DimenUtil.dipToPx(mContext, mContext.getResources().getDimension(id));
	}
	
	/**
	 * 为TextView设置文本
	 * @param comId
	 * @param txt
	 * @return 直接返回该类对象是为了可以实现链式编程
	 */
	public ViewHolderHelper setText(int comId, String txt){
		TextView tv = getComponent(comId);
		tv.setText(txt);
		return this;
	}
	
	public ViewHolderHelper setText(int comId, int resId){
		TextView tv = getComponent(comId);
		tv.setText(getString(resId));
		return this;
	}
	
	public ViewHolderHelper setTextColor(int comId, int resId){
		TextView tv = getComponent(comId);
		tv.setTextColor(getColor(resId));
		return this;
	}

	/**
	 * 向下兼容
	 * @param comId
	 * @param id
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public ViewHolderHelper setBackground(int comId, int resId) {
		View v = getComponent(comId);
		if (Build.VERSION.SDK_INT >= 16) {
			v.setBackground(getDrawable(resId));
		} else {
			v.setBackgroundDrawable(getDrawable(resId));
		}
		return this;
	}
	
	public ViewHolderHelper setBackgroundColor(int comId, int resId){
		View v = getComponent(comId);
		v.setBackgroundColor(getColor(resId));
		return this;
	}
	
	public ViewHolderHelper setBackgroundResource(int comId, int resId){
		View v = getComponent(comId);
		v.setBackgroundResource(resId);
		return this;
	}
	
	public ViewHolderHelper setTextSize(int comId, int resId){
		TextView tv = getComponent(comId);
		tv.setTextSize(getDimension(resId));
		return this;
	}

	/**
	 * 为ImageView设置图片资源
	 * @param comId
	 * @param resId
	 * @return
	 */
	public ViewHolderHelper setImageResource(int comId, int resId){
		ImageView iv = getComponent(comId);
		iv.setImageResource(resId);
		return this;
	}
	
	/**
	 * 为ImageView设置位图
	 * @param comId
	 * @param bmp
	 * @return
	 */
	public ViewHolderHelper setImageBitmap(int comId, Bitmap bmp){
		ImageView iv = getComponent(comId);
		iv.setImageBitmap(bmp);
		return this;
	}
	
	/**
	 * 为ImageView设置Drawable
	 * @param comId
	 * @param drawable
	 * @return
	 */
	public ViewHolderHelper setImageDrawable(int comId, Drawable drawable){
		ImageView iv = getComponent(comId);
		iv.setImageDrawable(drawable);
		return this;
	}
	
	public ViewHolderHelper setImageURI(int comId, Uri uri){
		ImageView iv = getComponent(comId);
		iv.setImageURI(uri);
		return this;
	}
	
	public ViewHolderHelper setImageURI(int comId, String url){
		// ImageView iv = getComponentOfItemView(comId);
		// Imageloader.getInstance().loadImg(view, url);
		return this;
	}
	
	public ViewHolderHelper setEnabled(int comId, boolean state){
		TextView tv = getComponent(comId);
		tv.setEnabled(state);
		return this;
	}
	
	public ViewHolderHelper setVisibility(int comId, int visible){
		View v = getComponent(comId);
		v.setVisibility(visible);
		return this;
	}
	
	public ViewHolderHelper setSelected(int comId, boolean select){
		View v = getComponent(comId);
		v.setSelected(select);
		return this;
	}
}
