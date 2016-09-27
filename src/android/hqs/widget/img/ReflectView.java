package android.hqs.widget.img;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.hqs.util.ImageConvertUtil;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ReflectView extends ImageView {

    private Bitmap mSrcBitmap;
    private Bitmap mRefBitmap;
    private Paint mPaint;

    private Matrix mMatrix;

    public ReflectView(Context context) {
        super(context);
        initView();
    }

    public ReflectView(Context context, AttributeSet attrs) {
    	this(context, attrs, 0);
    }

    public ReflectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
    	mMatrix = new Matrix();
        mMatrix.setScale(1, -1);
        mPaint = new Paint();
        createView();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        canvas.drawBitmap(mSrcBitmap, 0, 0, null);
        canvas.drawBitmap(mRefBitmap, 0, mSrcBitmap.getHeight(), null);
        canvas.drawRect(0, mRefBitmap.getHeight(), mRefBitmap.getWidth(),
                mRefBitmap.getHeight() * 2, mPaint);
    }
    
    private void createView(){
        if (getDrawable() == null) {
			return;
		}
        mSrcBitmap = ImageConvertUtil.drawableToBitmap(getDrawable());
        mRefBitmap = Bitmap.createBitmap(mSrcBitmap, 0, 0, mSrcBitmap.getWidth(),
                mSrcBitmap.getHeight(), mMatrix, true);
        mPaint.setShader(new LinearGradient(0, mSrcBitmap.getHeight(),
                0, mSrcBitmap.getHeight() * 1.4F,
                0XDD000000, 0X10000000, Shader.TileMode.CLAMP));
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
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
    
    // TODO 下面是公开的方法
    public void setImageMatrix(Matrix matrix) {
        mMatrix = matrix;
    }
}
