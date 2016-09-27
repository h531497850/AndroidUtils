package android.hqs.helper.sb;

import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.Gravity;
import android.widget.SeekBar;

public class SeekBarHelper {
	
	public static void setDrawableStyle(SeekBar sb, int backgroudId, int secondaryProgressId, int progressId){
		LayerDrawable mLayerDrawable = (LayerDrawable)sb.getProgressDrawable();
		Drawable[] outDrawables = new Drawable[mLayerDrawable.getNumberOfLayers()];
		List<Info> infos = new ArrayList<Info>();
		
		for (int i = 0; i < outDrawables.length; i++) {
			int id = mLayerDrawable.getId(i);
			switch (id) {
			case android.R.id.background:
				infos.add(new Info(i, id));
				outDrawables[i] = sb.getResources().getDrawable(backgroudId);
				break;
			case android.R.id.secondaryProgress:
				infos.add(new Info(i, id));
				outDrawables[i] = sb.getResources().getDrawable(secondaryProgressId);
				break;
			case android.R.id.progress:
				infos.add(new Info(i, id));
				ClipDrawable oidDrawable = (ClipDrawable) mLayerDrawable.getDrawable(i);
				Drawable drawable = sb.getResources().getDrawable(progressId);
				ClipDrawable proDrawable = new ClipDrawable(drawable, Gravity.START, ClipDrawable.HORIZONTAL);
				proDrawable.setLevel(oidDrawable.getLevel());
				outDrawables[i] = proDrawable;
				break;
			default:
				break;
			}
		}
		
		mLayerDrawable = new LayerDrawable(outDrawables);
		for (int i = 0; i < infos.size(); i++) {
			Info bean = infos.get(i);
			switch (bean.getId()) {
			case android.R.id.background:
				mLayerDrawable.setId(bean.getIndex(), android.R.id.background);
				break;
			case android.R.id.secondaryProgress:
				mLayerDrawable.setId(bean.getIndex(), android.R.id.secondaryProgress);
				break;
			case android.R.id.progress:
				mLayerDrawable.setId(bean.getIndex(), android.R.id.progress);
				break;
			default:
				break;
			}
		}
		sb.setProgressDrawable(mLayerDrawable);
	}
	

}
