package android.hqs.widget.img;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.hqs.gj.util.ImageConvertUtil;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageView;

public class MeshView extends ImageView {

    private int WIDTH = 200;
    private int HEIGHT = 200;
    private int COUNT = (WIDTH + 1) * (HEIGHT + 1);
    private float[] verts = new float[COUNT * 2];
    private float[] orig = new float[COUNT * 2];
    private Bitmap mBitmap;
    private float K = 1;

    public MeshView(Context context) {
        super(context);
        initView();
    }

    public MeshView(Context context, AttributeSet attrs) {
    	this(context, attrs, 0);
    }

    public MeshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        int index = 0;
        float bmWidth = mBitmap.getWidth();
        float bmHeight = mBitmap.getHeight();

        for (int i = 0; i < HEIGHT + 1; i++) {
            float fy = bmHeight * i / HEIGHT;
            for (int j = 0; j < WIDTH + 1; j++) {
                float fx = bmWidth * j / WIDTH;
                orig[index * 2 + 0] = verts[index * 2 + 0] = fx;
                orig[index * 2 + 1] = verts[index * 2 + 1] = fy + 200;
                index += 1;
            }
        }
        
        createView();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < HEIGHT + 1; i++) {
            for (int j = 0; j < WIDTH + 1; j++) {
                verts[(i * (WIDTH + 1) + j) * 2 + 0] += 0;
                float offsetY = (float) Math.sin((float) j / WIDTH * 2 * Math.PI + K * 2 * Math.PI);
                verts[(i * (WIDTH + 1) + j) * 2 + 1] =
                        orig[(i * (WIDTH + 1) + j) * 2 + 1] + offsetY * 50;
            }
        }
        K += 0.1F;
        canvas.drawBitmapMesh(mBitmap, WIDTH, HEIGHT,
                verts, 0, null, 0, null);
        invalidate();
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
}
