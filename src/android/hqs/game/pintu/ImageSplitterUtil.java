package android.hqs.game.pintu;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;

public class ImageSplitterUtil {

	/**
	 * @param bitmap
	 * @param piece
	 *            切成piece*piece块
	 * @return List<ImageBean>
	 */
	public static List<ImageBean> splitImage(Bitmap bitmap, int piece) {
		List<ImageBean> beans = new ArrayList<ImageBean>();

		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		int pieceWidth = Math.min(w, h) / piece;

		for (int i = 0; i < piece; i++) {
			for (int j = 0; j < piece; j++) {

				ImageBean bean = new ImageBean();
				bean.setIndex(j + i * piece);

				int x = j * pieceWidth;
				int y = i * pieceWidth;

				bean.setBitmap(Bitmap.createBitmap(bitmap, x, y,
						pieceWidth, pieceWidth));
				beans.add(bean);
			}
		}

		return beans;
	}

}
