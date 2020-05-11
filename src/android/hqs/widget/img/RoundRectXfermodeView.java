package android.hqs.widget.img;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.hqs.gj.util.ImageConvertUtil;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageView;

public class RoundRectXfermodeView extends ImageView {

    private Bitmap mBitmap;
    private Bitmap mOut;
    private Paint mPaint;
    
    public RoundRectXfermodeView(Context context) {
        super(context);
        init();
    }

    public RoundRectXfermodeView(Context context, AttributeSet attrs) {
    	this(context, attrs, 0);
    }

    public RoundRectXfermodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // 禁用硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        createView();
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mOut, 0, 0, null);
    }
    
    private void createView(){
        if (getDrawable() == null) {
			return;
		}
        mBitmap = ImageConvertUtil.drawableToBitmap(getDrawable());
        mOut = Bitmap.createBitmap(mBitmap.getWidth(),
                mBitmap.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mOut);
        // Dst 
        // 下面的方法是API 5.0，暂时注释掉
        //canvas.drawRoundRect(0, 0, mBitmap.getWidth(), mBitmap.getHeight(), 50, 50, mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        // Src
        canvas.drawBitmap(mBitmap, 0, 0, mPaint);
        mPaint.setXfermode(null);
    }
    
    @Override
    public void setImageBitmap(Bitmap bm) {
    	super.setImageBitmap(bm);
    	createView();
    }
    @Override
    public void setImageDrawable(Drawable drawable) {
    	super.setImageDrawable(drawable);
    	createView();
    }
    @Override
    public void setImageResource(int resId) {
    	super.setImageResource(resId);
    	createView();
    }
    @Override
    public void setImageURI(Uri uri) {
    	super.setImageURI(uri);
    	createView();
    }
    
}
