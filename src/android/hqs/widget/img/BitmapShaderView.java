package android.hqs.widget.img;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.hqs.gj.util.ImageConvertUtil;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageView;

public class BitmapShaderView extends ImageView {

    private Bitmap mBitmap;
    private Paint mPaint;
    private BitmapShader mBitmapShader;

    public BitmapShaderView(Context context) {
        super(context);
        init();
    }

    public BitmapShaderView(Context context, AttributeSet attrs) {
    	this(context, attrs, 0);
    }

    public BitmapShaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // 禁用硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        mBitmap = ImageConvertUtil.drawableToBitmap(getDrawable());
	}

	@Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(300, 200, 200, mPaint);
    }
	
	private void createView(){
		if (getDrawable() == null) {
			return;
		}
        mBitmap = ImageConvertUtil.drawableToBitmap(getDrawable());
        mBitmapShader = new BitmapShader(mBitmap,
                Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mPaint.setShader(mBitmapShader);
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
