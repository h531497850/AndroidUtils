package android.hqs.widget.img;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.hqs.util.ImageConvertUtil;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Administrator on 2015/2/1 0001.
 */
public class ImageMatrixView extends ImageView {

    private Bitmap mBitmap;
    private Matrix mMatrix;

    public ImageMatrixView(Context context) {
        super(context);
        initView();
    }

    public ImageMatrixView(Context context, AttributeSet attrs) {
    	this(context, attrs, 0);
    }

    public ImageMatrixView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        setImageMatrix(new Matrix());
        createView();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBitmap == null) {
			return;
		}
        canvas.drawBitmap(mBitmap, 0, 0, null);
        canvas.drawBitmap(mBitmap, mMatrix, null);
    }
    
    private void createView(){
        if (getDrawable() == null) {
			return;
		}
        mBitmap = ImageConvertUtil.drawableToBitmap(getDrawable());
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
